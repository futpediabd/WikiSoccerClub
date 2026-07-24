package com.wikisoccerclub.ui.scouting

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.scouting.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ScoutingUiState(
    val scouts: List<ScoutProfile> =
        emptyList(),
    val assignments: List<ScoutingAssignment> =
        emptyList(),
    val reports: List<ScoutingReport> =
        emptyList(),
    val selectedReport: ScoutingReport? = null,
    val error: String? = null
)

class ScoutingViewModel(
    private val repository: ScoutingRepository =
        ScoutingRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ScoutingUiState())

    val uiState: StateFlow<ScoutingUiState> =
        _uiState.asStateFlow()

    fun configurePlayers(
        players: List<ScoutedPlayer>
    ) {
        repository.savePlayers(players)
        refresh()
    }

    fun hireScout(scout: ScoutProfile) {
        repository.saveScout(scout)
        refresh()
    }

    fun dismissScout(scoutId: String) {
        repository.removeScout(scoutId)
        refresh()
    }

    fun createAssignment(
        assignment: ScoutingAssignment,
        currentDay: Int
    ) {
        runCatching {
            ScoutingEngine.startAssignment(
                assignment,
                currentDay
            )
        }.onSuccess {
            repository.saveAssignment(it)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun advanceDay(currentDay: Int) {
        repository.assignmentsByStatus(
            ScoutingStatus.IN_PROGRESS
        ).filter {
            ScoutingEngine.canComplete(
                it,
                currentDay
            )
        }.forEach { assignment ->
            val completed =
                ScoutingEngine.completeAssignment(
                    assignment,
                    currentDay
                )

            repository.saveAssignment(completed)

            val scout =
                repository.findScout(
                    completed.scoutId
                ) ?: return@forEach

            val reports =
                ScoutingEngine.generateReports(
                    scout = scout,
                    assignment = completed,
                    candidates =
                        repository.allPlayers(),
                    currentDay = currentDay
                )

            repository.saveReports(reports)
        }

        refresh()
    }

    fun selectReport(playerId: String) {
        _uiState.value =
            _uiState.value.copy(
                selectedReport =
                    repository.reportByPlayer(
                        playerId
                    )
            )
    }

    private fun refresh() {
        _uiState.value =
            _uiState.value.copy(
                scouts = repository.allScouts(),
                assignments =
                    repository.allAssignments(),
                reports =
                    repository.allReports(),
                error = null
            )
    }

    private fun showError(message: String?) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message ?: "Erro desconhecido."
            )
    }
}
