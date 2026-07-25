package com.wikisoccerclub.data.competition.runtime

data class RuntimeMatch(
    val matchId: String,
    val competitionId: String,
    val season: Int,
    val round: Int,
    val day: Int,
    val homeClubId: String,
    val awayClubId: String,
    val status: RuntimeMatchStatus = RuntimeMatchStatus.SCHEDULED,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null
)

enum class RuntimeMatchStatus {
    SCHEDULED,
    IN_PROGRESS,
    FINISHED,
    POSTPONED
}

data class RuntimeMatchResult(
    val matchId: String,
    val homeGoals: Int,
    val awayGoals: Int
)

data class RuntimeCompetitionState(
    val competitionId: String,
    val season: Int,
    val currentDay: Int,
    val matches: List<RuntimeMatch>
)

data class RuntimeRoundExecutionResult(
    val competitionId: String,
    val season: Int,
    val day: Int,
    val finishedMatches: List<RuntimeMatch>,
    val remainingMatches: Int
)
