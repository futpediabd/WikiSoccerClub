package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.competition.CompetitionStandings
import com.wikisoccerclub.data.transfer.CareerDate

data class PromotionRelegationRule(
    val competitionId: String,
    val nextDivisionId: String? = null,
    val previousDivisionId: String? = null,
    val promotionPlaces: Int = 0,
    val relegationPlaces: Int = 0
)

data class CompetitionSeasonOutcome(
    val competitionId: String,
    val championClubId: String?,
    val championClubName: String?,
    val promotedClubIds: List<String>,
    val relegatedClubIds: List<String>,
    val finalStandings: CompetitionStandings
)

data class CareerSeasonSummary(
    val seasonYear: Int,
    val finishedAt: CareerDate,
    val outcomes: List<CompetitionSeasonOutcome>,
    val champions: Map<String, String>,
    val promotedClubIds: List<String>,
    val relegatedClubIds: List<String>
)

data class DivisionMembershipChange(
    val clubId: String,
    val fromCompetitionId: String,
    val toCompetitionId: String,
    val type: DivisionChangeType
)

enum class DivisionChangeType { PROMOTION, RELEGATION }
