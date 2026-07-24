package com.wikisoccerclub.data.competition

data class CompetitionMatch(
    val id: String,
    val round: Int,
    val homeTeamId: String,
    val awayTeamId: String,
    val played: Boolean = false,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null
)

data class CompetitionProgress(
    val competitionId: String,
    val currentRound: Int = 1,
    val matches: List<CompetitionMatch> = emptyList(),
    val standings: CompetitionStandings = CompetitionStandings(
        competitionId = competitionId
    )
)
