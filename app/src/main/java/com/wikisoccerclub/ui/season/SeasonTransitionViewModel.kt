package com.wikisoccerclub.ui.season

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.CompetitionOutcome
import com.wikisoccerclub.data.season.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SeasonTransitionUiState(
    val currentSeason: Season,
    val transition: SeasonTransitionResult? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class SeasonTransitionViewModel(
    private val repository: SeasonRepository =
        SeasonRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SeasonTransitionUiState(
            currentSeason = repository.current()
        )
    )
    val uiState: StateFlow<SeasonTransitionUiState> =
        _uiState.asStateFlow()

    fun finishCurrentSeason() {
        val finished = repository.current().copy(
            finished = true
        )
        repository.saveSeason(finished)
        _uiState.value = _uiState.value.copy(
            currentSeason = finished,
            error = null
        )
    }

    fun startNextSeason(
        outcomes: Map<String, CompetitionOutcome>,
        champions: Map<String, String>
    ) {
        _uiState.value = _uiState.value.copy(
            loading = true,
            error = null
        )

        runCatching {
            SeasonTransitionEngine.execute(
                currentSeason = repository.current(),
                divisions = repository.divisions(),
                outcomes = outcomes,
                champions = champions
            )
        }.onSuccess { transition ->
            repository.saveSummary(
                transition.archivedSummary
            )
            repository.saveDivisions(
                transition.updatedDivisions
            )

            val nextSeason = SeasonEngine.next(
                repository.current()
            )
            repository.saveSeason(nextSeason)

            _uiState.value = SeasonTransitionUiState(
                currentSeason = nextSeason,
                transition = transition
            )
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                loading = false,
                error = error.message
            )
        }
    }

    fun configureDivisions(
        divisions: List<LeagueDivisionState>
    ) {
        repository.saveDivisions(divisions)
    }
}
