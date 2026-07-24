package com.wikisoccerclub.data.competition.model

data class CompetitionSaveState(
    val competitionId: String,
    val season: Int,
    val currentPhase: String,
    val participantClubIds: List<String>,
    val activeClubIds: List<String>,
    val eliminatedClubIds: List<String>,
    val completedMatches: List<CompetitionMatchResult>,
    val championClubId: String? = null,
    val runnerUpClubId: String? = null,
    val isFinished: Boolean = false
)
