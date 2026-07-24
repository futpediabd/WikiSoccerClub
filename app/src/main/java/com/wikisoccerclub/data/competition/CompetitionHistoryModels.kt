package com.wikisoccerclub.data.competition

data class CompetitionSeasonRecord(
    val competitionId: String,
    val season: Int,
    val championTeamId: String,
    val runnerUpTeamId: String?,
    val promotedTeamIds: List<String>,
    val relegatedTeamIds: List<String>
)

data class CompetitionHistory(
    val records: List<CompetitionSeasonRecord> = emptyList()
)
