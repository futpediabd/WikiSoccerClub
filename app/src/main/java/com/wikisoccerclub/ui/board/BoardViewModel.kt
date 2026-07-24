package com.wikisoccerclub.ui.board

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.board.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BoardUiState(
    val objectives: List<BoardObjective> =
        emptyList(),
    val evaluation: BoardEvaluation? = null,
    val clubReputation: ClubReputation? = null,
    val managerReputation: ManagerReputation? = null,
    val error: String? = null
)

class BoardViewModel(
    private val repository: BoardRepository =
        BoardRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(BoardUiState())

    val uiState: StateFlow<BoardUiState> =
        _uiState.asStateFlow()

    fun configure(
        objectives: List<BoardObjective>,
        clubReputation: ClubReputation,
        managerReputation: ManagerReputation
    ) {
        objectives.forEach {
            BoardObjectiveEngine.validate(it)
                .getOrThrow()
            repository.saveObjective(it)
        }
        repository.saveClubReputation(
            clubReputation
        )
        repository.saveManagerReputation(
            managerReputation
        )
        refresh(
            clubId = clubReputation.clubId,
            seasonYear = objectives
                .firstOrNull()?.seasonYear ?: 0,
            managerId = managerReputation.managerId
        )
    }

    fun updateObjective(
        objectiveId: String,
        currentValue: Int,
        currentDay: Int? = null
    ) {
        val objective =
            repository.findObjective(objectiveId)
                ?: return showError(
                    "Objetivo não encontrado."
                )

        runCatching {
            BoardObjectiveEngine.updateProgress(
                objective = objective,
                value = currentValue,
                currentDay = currentDay
            )
        }.onSuccess {
            repository.saveObjective(it)
            refresh(
                clubId = it.clubId,
                seasonYear = it.seasonYear
            )
        }.onFailure {
            showError(it.message)
        }
    }

    fun evaluateBoard(
        clubId: String,
        seasonYear: Int,
        leagueFormScore: Int,
        financialScore: Int,
        dressingRoomScore: Int
    ) {
        runCatching {
            BoardEvaluationEngine.evaluate(
                clubId = clubId,
                seasonYear = seasonYear,
                objectives =
                    repository.objectivesByClub(
                        clubId,
                        seasonYear
                    ),
                leagueFormScore = leagueFormScore,
                financialScore = financialScore,
                dressingRoomScore =
                    dressingRoomScore
            )
        }.onSuccess {
            repository.saveEvaluation(it)
            refresh(clubId, seasonYear)
        }.onFailure {
            showError(it.message)
        }
    }

    private fun refresh(
        clubId: String,
        seasonYear: Int,
        managerId: String? = null
    ) {
        _uiState.value = BoardUiState(
            objectives =
                repository.objectivesByClub(
                    clubId,
                    seasonYear
                ),
            evaluation =
                repository.latestEvaluation(clubId),
            clubReputation =
                repository.findClubReputation(clubId),
            managerReputation =
                managerId?.let {
                    repository.findManagerReputation(it)
                } ?: _uiState.value.managerReputation
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
