package com.wikisoccerclub.data.competition.runtime

data class CompetitionTableEntry(
    val clubId: String,
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

data class CompetitionTable(
    val competitionId: String,
    val season: Int,
    val entries: List<CompetitionTableEntry>
)

data class CompetitionTableUpdateResult(
    val competitionId: String,
    val season: Int,
    val updatedTable: CompetitionTable,
    val processedMatches: Int
)
