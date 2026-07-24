package com.wikisoccerclub.data.competition

enum class MatchEventType {
    GOAL,
    YELLOW_CARD,
    RED_CARD,
    INJURY,
    SUSPENSION_SERVED
}

data class CompetitionTeam(
    val id: String,
    val name: String,
    val strength: Int = 50,
    val userControlled: Boolean = false,
    val players: List<CompetitionPlayer> = emptyList()
)

data class CompetitionPlayer(
    val id: String,
    val teamId: String,
    val name: String,
    val position: String,
    val finishing: Int,
    val discipline: Int = 60,
    val fitness: Int = 80
)

data class MatchEvent(
    val id: String,
    val type: MatchEventType,
    val playerId: String,
    val teamId: String,
    val minute: Int,
    val injuryRounds: Int = 0
)

data class CompetitionMatch(
    val id: String,
    val round: Int,
    val homeTeamId: String,
    val awayTeamId: String,
    val homeGoals: Int = 0,
    val awayGoals: Int = 0,
    val homeShots: Int = 0,
    val awayShots: Int = 0,
    val homeShotsOnTarget: Int = 0,
    val awayShotsOnTarget: Int = 0,
    val homePossession: Int = 50,
    val awayPossession: Int = 50,
    val played: Boolean = false,
    val events: List<MatchEvent> = emptyList()
)

data class StandingRow(
    val position: Int,
    val teamId: String,
    val teamName: String,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val points: Int
)

data class TopScorerRow(
    val position: Int,
    val playerId: String,
    val playerName: String,
    val teamName: String,
    val goals: Int
)

data class DisciplineRow(
    val position: Int,
    val playerId: String,
    val playerName: String,
    val teamName: String,
    val yellowCards: Int,
    val redCards: Int,
    val suspensionRounds: Int
)

data class InjuryRow(
    val playerId: String,
    val playerName: String,
    val teamName: String,
    val injuryRound: Int,
    val returnRound: Int,
    val remainingRounds: Int
)

data class CompetitionSnapshot(
    val name: String,
    val currentRound: Int,
    val totalRounds: Int,
    val completed: Boolean,
    val teams: List<CompetitionTeam>,
    val matches: List<CompetitionMatch>,
    val standings: List<StandingRow>,
    val topScorers: List<TopScorerRow>,
    val discipline: List<DisciplineRow>,
    val injuries: List<InjuryRow>,
    val userTeamAvailability: TeamAvailability?
)
