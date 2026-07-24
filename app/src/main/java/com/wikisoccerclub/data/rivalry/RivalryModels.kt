package com.wikisoccerclub.data.rivalry

enum class RivalryScope {
    LOCAL,
    STATE,
    REGIONAL,
    NATIONAL,
    CONTINENTAL,
    INTERNATIONAL
}

enum class RivalryIntensity {
    LOW,
    MODERATE,
    HIGH,
    FIERCE,
    HISTORIC
}

data class ClubRivalry(
    val id: String,
    val clubAId: String,
    val clubAName: String,
    val clubBId: String,
    val clubBName: String,
    val scope: RivalryScope,
    val score: Int = 0,
    val matches: Int = 0,
    val clubAWins: Int = 0,
    val draws: Int = 0,
    val clubBWins: Int = 0,
    val clubAGoals: Int = 0,
    val clubBGoals: Int = 0,
    val finalsPlayed: Int = 0,
    val titleDecisions: Int = 0,
    val lastSeasonYear: Int? = null
) {
    val intensity: RivalryIntensity
        get() = when {
            score >= 90 -> RivalryIntensity.HISTORIC
            score >= 70 -> RivalryIntensity.FIERCE
            score >= 45 -> RivalryIntensity.HIGH
            score >= 20 -> RivalryIntensity.MODERATE
            else -> RivalryIntensity.LOW
        }
}

data class RivalryMatchInput(
    val matchId: String,
    val seasonYear: Int,
    val competitionId: String,
    val competitionName: String,
    val clubAId: String,
    val clubAName: String,
    val clubBId: String,
    val clubBName: String,
    val clubAGoals: Int,
    val clubBGoals: Int,
    val isKnockout: Boolean = false,
    val isFinal: Boolean = false,
    val decidedTitle: Boolean = false,
    val attendance: Int = 0
)

data class RivalrySummary(
    val rivalry: ClubRivalry,
    val biggestClubAWin: String? = null,
    val biggestClubBWin: String? = null,
    val highestAttendance: Int = 0,
    val recentMatches: List<RivalryMatchInput> = emptyList()
)
