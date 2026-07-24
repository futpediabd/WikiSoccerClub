package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.data.match.CompletedMatchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StandingsUiState(
    val progress: CompetitionProgress? = null,
    val error: String? = null
)

class StandingsViewModel(
    private val repository: CompetitionProgressRepository =
        CompetitionProgressRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(StandingsUiState())
    val uiState: StateFlow<StandingsUiState> =
        _uiState.asStateFlow()

    fun initialize(progress: CompetitionProgress) {
        repository.save(progress)
        _uiState.value = StandingsUiState(progress = progress)
    }

    fun applyResult(
        competitionId: String,
        result: CompletedMatchResult,
        teamNames: Map<String, String>
    ) {
        val current = repository.find(competitionId)

        if (current == null) {
            _uiState.value = StandingsUiState(
                error = "Competição não encontrada."
            )
            return
        }

        val updated = CompetitionProgressEngine.applyMatchResult(
            progress = current,
            result = result,
            teamNames = teamNames
        )

        repository.save(updated)
        _uiState.value = StandingsUiState(progress = updated)
    }

    fun load(competitionId: String) {
        val progress = repository.find(competitionId)

        _uiState.value = StandingsUiState(
            progress = progress,
            error = if (progress == null) {
                "Competição não encontrada."
            } else {
                null
            }
        )
    }
}
