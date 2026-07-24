package com.wikisoccerclub.data.competition.model

data class CompetitionMatchResult(
    val matchId: String,
    val competitionId: String,
    val season: Int,
    val phase: String,
    val groupName: String? = null,
    val homeClubId: String,
    val awayClubId: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val homePenaltyGoals: Int? = null,
    val awayPenaltyGoals: Int? = null,
    val leg: Int = 1,
    val roundNumber: Int? = null
)

data class GroupStandingRow(
    val clubId: String,
    val played: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val points: Int = 0,
    val disciplinaryPoints: Int = 0
) {
    val goalDifference: Int get() = goalsFor - goalsAgainst
}

data class GroupQualificationResult(
    val groupName: String,
    val table: List<GroupStandingRow>,
    val qualifiedClubIds: List<String>
)

data class KnockoutQualification(
    val phase: String,
    val qualifiedClubIds: List<String>,
    val eliminatedClubIds: List<String>
)

data class CompetitionProgressSnapshot(
    val competitionId: String,
    val season: Int,
    val currentPhase: String,
    val completedMatchIds: List<String>,
    val activeClubIds: List<String>,
    val eliminatedClubIds: List<String>,
    val championClubId: String? = null,
    val runnerUpClubId: String? = null
)
