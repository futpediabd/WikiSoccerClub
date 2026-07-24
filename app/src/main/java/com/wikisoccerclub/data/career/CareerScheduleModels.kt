package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.transfer.CareerDate

enum class ScheduledMatchStatus { SCHEDULED, READY_FOR_USER, SIMULATED, COMPLETED }

data class ScheduledCareerMatch(
    val id: String,
    val date: CareerDate,
    val competitionId: String,
    val competitionName: String,
    val homeClubId: String,
    val homeClubName: String,
    val awayClubId: String,
    val awayClubName: String,
    val userClubId: String? = null,
    val roundLabel: String = "",
    val status: ScheduledMatchStatus = ScheduledMatchStatus.SCHEDULED,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null
) {
    val involvesUserClub: Boolean
        get() = userClubId != null && (homeClubId == userClubId || awayClubId == userClubId)

    val userIsHome: Boolean get() = userClubId != null && homeClubId == userClubId

    val opponentName: String?
        get() = when (userClubId) {
            homeClubId -> awayClubName
            awayClubId -> homeClubName
            else -> null
        }
}

data class CareerMatchDay(
    val date: CareerDate,
    val matches: List<ScheduledCareerMatch>,
    val userMatches: List<ScheduledCareerMatch>,
    val aiMatches: List<ScheduledCareerMatch>
)

data class SimulatedCareerMatch(
    val matchId: String,
    val homeGoals: Int,
    val awayGoals: Int
)

data class CareerAdvanceToMatchResult(
    val reachedDate: CareerDate,
    val advancedDays: Int,
    val matchDay: CareerMatchDay?,
    val dailyResults: List<CareerAdvanceResult>
)
