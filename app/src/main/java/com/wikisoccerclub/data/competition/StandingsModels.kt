package com.wikisoccerclub.data.competition

data class StandingEntry(
    val teamId: String,
    val teamName: String,
    val played: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0
) {
    val goalDifference: Int
        get() = goalsFor - goalsAgainst
}

data class CompetitionStandings(
    val competitionId: String,
    val entries: List<StandingEntry> = emptyList()
)
