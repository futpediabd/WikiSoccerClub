package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CompetitionUiState(
    val competitionName: String = "",
    val currentRound: Int = 1,
    val totalRounds: Int = 0,
    val completed: Boolean = false,
    val teams: List<CompetitionTeam> = emptyList(),
    val matches: List<CompetitionMatch> = emptyList(),
    val currentRoundMatches: List<CompetitionMatch> = emptyList(),
    val standings: List<StandingRow> = emptyList(),
    val topScorers: List<TopScorerRow> = emptyList(),
    val discipline: List<DisciplineRow> = emptyList(),
    val injuries: List<InjuryRow> = emptyList(),
    val userTeamAvailability: TeamAvailability? = null,
    val lineup: CompetitionLineup = CompetitionLineup(
        formation = "4-3-3",
        tacticalStyle = TacticalStyle.BALANCED,
        starters = emptyList(),
        substitutes = emptyList()
    ),
    val lineupValidation: LineupValidation = LineupValidation(
        valid = false,
        message = "Escalação incompleta."
    )
)

class CompetitionViewModel(
    private val repository: CompetitionRepository = CompetitionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompetitionUiState())
    val uiState: StateFlow<CompetitionUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun simulateCurrentRound() {
        repository.simulateCurrentRound()
        refresh()
    }

    fun changeTacticalStyle(style: TacticalStyle) {
        updateLineup(
            repository.automaticUserLineup(style)
        )
    }

    fun regenerateAutomaticLineup() {
        updateLineup(
            repository.automaticUserLineup(
                _uiState.value.lineup.tacticalStyle
            )
        )
    }

    fun moveToStarters(player: CompetitionPlayer) {
        updateLineup(
            LineupEditor.moveToStarters(
                lineup = _uiState.value.lineup,
                player = player
            )
        )
    }

    fun moveToSubstitutes(player: CompetitionPlayer) {
        updateLineup(
            LineupEditor.moveToSubstitutes(
                lineup = _uiState.value.lineup,
                player = player
            )
        )
    }

    fun removeFromLineup(playerId: String) {
        updateLineup(
            LineupEditor.removePlayer(
                lineup = _uiState.value.lineup,
                playerId = playerId
            )
        )
    }

    fun swapPlayers(
        starterId: String,
        substituteId: String
    ) {
        updateLineup(
            LineupEditor.swapPlayers(
                lineup = _uiState.value.lineup,
                starterId = starterId,
                substituteId = substituteId
            )
        )
    }

    fun matchById(matchId: String): CompetitionMatch? =
        repository.matchById(matchId)

    private fun updateLineup(lineup: CompetitionLineup) {
        _uiState.value = _uiState.value.copy(
            lineup = lineup,
            lineupValidation = LineupEditor.validate(lineup)
        )
    }

    private fun refresh() {
        val snapshot = repository.snapshot()
        val automaticLineup = repository.automaticUserLineup()

        _uiState.value = CompetitionUiState(
            competitionName = snapshot.name,
            currentRound = snapshot.currentRound,
            totalRounds = snapshot.totalRounds,
            completed = snapshot.completed,
            teams = snapshot.teams,
            matches = snapshot.matches,
            currentRoundMatches = repository.currentRoundMatches(),
            standings = snapshot.standings,
            topScorers = snapshot.topScorers,
            discipline = snapshot.discipline,
            injuries = snapshot.injuries,
            userTeamAvailability = snapshot.userTeamAvailability,
            lineup = automaticLineup,
            lineupValidation = LineupEditor.validate(automaticLineup)
        )
    }
}
