package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.competition.CompetitionStandings

data class CompetitionRoundSummary(
    val competitionId: String,
    val competitionName: String,
    val roundLabel: String,
    val matches: List<ScheduledCareerMatch>,
    val completedMatches: Int,
    val pendingMatches: Int,
    val standings: CompetitionStandings
)

data class MatchResultRegistration(
    val match: ScheduledCareerMatch,
    val standings: CompetitionStandings,
    val roundCompleted: Boolean
)
