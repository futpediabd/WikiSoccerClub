package com.wikisoccerclub.data.records

import com.wikisoccerclub.data.headtohead.HeadToHeadMatch

enum class StreakType {
    UNBEATEN,
    WINS,
    LOSSES,
    WINLESS,
    CLEAN_SHEETS,
    SCORING
}

data class ClubStreak(
    val clubId: String,
    val clubName: String,
    val type: StreakType,
    val length: Int,
    val startMatch: HeadToHeadMatch,
    val endMatch: HeadToHeadMatch,
    val active: Boolean,
    val competitionId: String? = null
)

data class ClubStreakSummary(
    val clubId: String,
    val clubName: String,
    val current: Map<StreakType, ClubStreak?>,
    val records: Map<StreakType, ClubStreak?>,
    val matchesAnalyzed: Int,
    val competitionFilter: String? = null
)
