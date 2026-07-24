package com.wikisoccerclub.data.competition

data class CompetitionOutcome(
    val competitionId: String,
    val championTeamId: String?,
    val promotedTeamIds: List<String> = emptyList(),
    val relegatedTeamIds: List<String> = emptyList(),
    val continentalQualifiedTeamIds: List<String> = emptyList(),
    val completed: Boolean = false
)
