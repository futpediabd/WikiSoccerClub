package com.wikisoccerclub.data.competition.model

data class PromotionRelegationRule(
    val competitionId: String,
    val promotedPositions: IntRange? = null,
    val promotionPlayoffPositions: IntRange? = null,
    val relegationPlayoffPositions: IntRange? = null,
    val relegatedPositions: IntRange? = null
)

data class ClubSeasonMovement(
    val clubId: String,
    val fromCompetitionId: String,
    val toCompetitionId: String,
    val reason: String
)

data class SeasonTransitionResult(
    val seasonFinished: Int,
    val nextSeason: Int,
    val movements: List<ClubSeasonMovement>,
    val qualifiedByCompetition: Map<String, List<String>>,
    val warnings: List<String> = emptyList()
)

data class SeasonCalendarRegistration(
    val competitionId: String,
    val season: Int,
    val matchDays: List<Int>
)
