package com.wikisoccerclub.ui.youth

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.youth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class YouthUiState(
    val selectedPosition:
        YouthPositionFilter =
        YouthPositionFilter.ALL,
    val latestTryout:
        YouthTryoutResult? = null,
    val tryoutHistory:
        List<YouthTryoutResult> =
        emptyList(),
    val error: String? = null
)

class YouthViewModel(
    private val repository:
        YouthRepository =
        YouthRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(YouthUiState())

    val uiState:
        StateFlow<YouthUiState> =
        _uiState.asStateFlow()

    fun selectPosition(
        position: YouthPositionFilter
    ) {
        _uiState.value =
            _uiState.value.copy(
                selectedPosition = position,
                error = null
            )
    }

    fun runTryout(
        config: YouthTryoutConfig,
        nationalityPool: List<String>,
        firstNames: List<String>,
        lastNames: List<String>,
        currentDay: Int
    ) {
        runCatching {
            YouthTryoutEngine.runTryout(
                config =
                    config.copy(
                        positionFilter =
                            _uiState.value
                                .selectedPosition
                    ),
                nationalityPool =
                    nationalityPool,
                firstNames = firstNames,
                lastNames = lastNames,
                currentDay = currentDay
            )
        }.onSuccess { result ->
            repository.saveTryout(result)
            refresh(result.clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun signCandidate(
        clubId: String,
        tryoutId: String,
        candidateId: String,
        currentSquadSize: Int,
        maximumSquadSize: Int = 40
    ) {
        val result =
            repository.findTryout(tryoutId)
                ?: return showError(
                    "Peneira não encontrada."
                )

        val candidate =
            result.candidates.find {
                it.id == candidateId
            } ?: return showError(
                "Jogador não encontrado."
            )

        runCatching {
            YouthTryoutEngine.signCandidate(
                candidate = candidate,
                currentSquadSize =
                    currentSquadSize,
                maximumSquadSize =
                    maximumSquadSize
            )
        }.onSuccess {
            repository.updateCandidate(
                tryoutId,
                it
            )
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun rejectCandidate(
        clubId: String,
        tryoutId: String,
        candidateId: String
    ) {
        val result =
            repository.findTryout(tryoutId)
                ?: return showError(
                    "Peneira não encontrada."
                )

        val candidate =
            result.candidates.find {
                it.id == candidateId
            } ?: return showError(
                "Jogador não encontrado."
            )

        runCatching {
            YouthTryoutEngine
                .rejectCandidate(candidate)
        }.onSuccess {
            repository.updateCandidate(
                tryoutId,
                it
            )
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun loadClub(
        clubId: String
    ) {
        refresh(clubId)
    }

    private fun refresh(
        clubId: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                latestTryout =
                    repository.latestTryout(
                        clubId
                    ),
                tryoutHistory =
                    repository.tryoutsByClub(
                        clubId
                    ),
                error = null
            )
    }

    private fun showError(
        message: String?
    ) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message
                        ?: "Erro desconhecido."
            )
    }
}
