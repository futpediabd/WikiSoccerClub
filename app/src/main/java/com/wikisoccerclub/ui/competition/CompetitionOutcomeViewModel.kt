package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CompetitionOutcomeUiState(
    val outcome: CompetitionOutcome? = null,
    val historySaved: Boolean = false,
    val error: String? = null
)

class CompetitionOutcomeViewModel(
    private val historyRepository: CompetitionHistoryRepository =
        CompetitionHistoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CompetitionOutcomeUiState()
    )
    val uiState: StateFlow<CompetitionOutcomeUiState> =
        _uiState.asStateFlow()

    fun finishCompetition(
        progress: CompetitionProgress,
        season: Int,
        rules: List<CompetitionZoneRule>
    ) {
        val outcome = CompetitionOutcomeEngine.calculate(
            progress = progress,
            zoneRules = rules
        )

        if (!outcome.completed ||
            outcome.championTeamId == null
        ) {
            _uiState.value = CompetitionOutcomeUiState(
                outcome = outcome,
                error = "A competição ainda não foi concluída."
            )
            return
        }

        val runnerUp = progress.standings.entries
            .getOrNull(1)
            ?.teamId

        historyRepository.add(
            CompetitionSeasonRecord(
                competitionId = progress.competitionId,
                season = season,
                championTeamId = outcome.championTeamId,
                runnerUpTeamId = runnerUp,
                promotedTeamIds = outcome.promotedTeamIds,
                relegatedTeamIds = outcome.relegatedTeamIds
            )
        )

        _uiState.value = CompetitionOutcomeUiState(
            outcome = outcome,
            historySaved = true
        )
    }

    fun history(
        competitionId: String
    ): List<CompetitionSeasonRecord> =
        historyRepository.byCompetition(competitionId)
}
