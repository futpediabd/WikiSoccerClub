package com.wikisoccerclub.ui.records

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.records.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RecordsUiState(
    val records: List<FootballRecord> =
        emptyList(),
    val hallOfFame: List<HallOfFameEntry> =
        emptyList(),
    val awards: List<SeasonAward> =
        emptyList(),
    val error: String? = null
)

class RecordsViewModel(
    private val repository: RecordsRepository =
        RecordsRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(RecordsUiState())

    val uiState: StateFlow<RecordsUiState> =
        _uiState.asStateFlow()

    fun savePlayerStats(stats: PlayerCareerStats) {
        repository.savePlayerStats(stats)
    }

    fun saveClubStats(stats: ClubCareerStats) {
        repository.saveClubStats(stats)
    }

    fun generateRecords(
        scope: RecordScope,
        scopeId: String? = null,
        matches: List<MatchRecordInput> = emptyList()
    ) {
        val generated =
            RecordEngine.playerRecords(
                repository.allPlayerStats(),
                scope,
                scopeId
            ) +
                RecordEngine.clubRecords(
                    repository.allClubStats(),
                    scope,
                    scopeId
                ) +
                RecordEngine.matchRecords(
                    matches,
                    scope,
                    scopeId
                )

        repository.saveRecords(generated)
        refresh()
    }

    fun evaluateHallOfFame(
        inductionYear: Int
    ) {
        repository.allPlayerStats()
            .mapNotNull {
                HallOfFameEngine.createPlayerEntry(
                    stats = it,
                    inductionYear = inductionYear
                )
            }
            .forEach(
                repository::saveHallOfFameEntry
            )

        repository.allClubStats()
            .mapNotNull {
                HallOfFameEngine.createClubEntry(
                    stats = it,
                    inductionYear = inductionYear
                )
            }
            .forEach(
                repository::saveHallOfFameEntry
            )

        refresh()
    }

    fun saveAward(award: SeasonAward) {
        repository.saveAward(award)
        _uiState.value = _uiState.value.copy(
            awards =
                repository.awardsBySeason(
                    award.seasonYear
                )
        )
    }

    fun filter(
        scope: RecordScope? = null,
        category: RecordCategory? = null
    ) {
        _uiState.value = _uiState.value.copy(
            records = repository.records(
                scope = scope,
                category = category
            )
        )
    }

    private fun refresh() {
        _uiState.value = RecordsUiState(
            records = repository.records(),
            hallOfFame =
                repository.hallOfFameEntries(),
            awards = _uiState.value.awards
        )
    }
}
