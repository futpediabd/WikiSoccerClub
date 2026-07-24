package com.wikisoccerclub.ui.rivalry

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.rivalry.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RivalryUiState(
    val rivalries: List<ClubRivalry> =
        emptyList(),
    val selectedSummary: RivalrySummary? = null,
    val error: String? = null
)

class RivalryViewModel(
    private val repository: RivalryRepository =
        RivalryRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(RivalryUiState())

    val uiState: StateFlow<RivalryUiState> =
        _uiState.asStateFlow()

    fun registerMatch(
        input: RivalryMatchInput,
        firstLocation: ClubLocationProfile,
        secondLocation: ClubLocationProfile
    ) {
        runCatching {
            val existing =
                repository.findRivalry(
                    input.clubAId,
                    input.clubBId
                )

            val rivalry = existing ?: run {
                val scope =
                    RivalryDiscoveryEngine.inferScope(
                        firstLocation,
                        secondLocation
                    )

                RivalryEngine.create(
                    input = input,
                    scope = scope
                ).copy(
                    score =
                        RivalryDiscoveryEngine.initialScore(
                            scope = scope,
                            matchesPlayed = 0,
                            finalsPlayed = 0,
                            titleDecisions = 0
                        )
                )
            }

            val updated =
                RivalryEngine.registerMatch(
                    rivalry = rivalry,
                    input = input
                )

            repository.saveMatch(input)
            repository.saveRivalry(updated)
        }.onSuccess {
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun selectRivalry(rivalryId: String) {
        val rivalry =
            repository.allRivalries()
                .firstOrNull {
                    it.id == rivalryId
                }
                ?: return showError(
                    "Rivalidade não encontrada."
                )

        _uiState.value = _uiState.value.copy(
            selectedSummary =
                RivalrySummaryEngine.build(
                    rivalry = rivalry,
                    matches =
                        repository.matchesFor(rivalry)
                ),
            error = null
        )
    }

    fun filterByClub(clubId: String) {
        _uiState.value = _uiState.value.copy(
            rivalries =
                repository.rivalriesByClub(clubId),
            selectedSummary = null
        )
    }

    fun advanceSeason(currentSeasonYear: Int) {
        repository.allRivalries()
            .map {
                RivalryEngine.decay(
                    rivalry = it,
                    currentSeasonYear =
                        currentSeasonYear
                )
            }
            .forEach(repository::saveRivalry)

        refresh()
    }

    private fun refresh() {
        _uiState.value = RivalryUiState(
            rivalries =
                repository.allRivalries(),
            selectedSummary =
                _uiState.value.selectedSummary
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
