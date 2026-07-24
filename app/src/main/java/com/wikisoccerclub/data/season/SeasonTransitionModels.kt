package com.wikisoccerclub.data.season

data class LeagueDivisionState(
    val competitionId: String,
    val divisionLevel: Int,
    val teamIds: List<String>
)

data class PromotionRelegationMovement(
    val teamId: String,
    val fromCompetitionId: String,
    val toCompetitionId: String,
    val type: MovementType
)

enum class MovementType {
    PROMOTION,
    RELEGATION
}

data class SeasonTransitionResult(
    val previousYear: Int,
    val newYear: Int,
    val updatedDivisions: List<LeagueDivisionState>,
    val movements: List<PromotionRelegationMovement>,
    val archivedSummary: SeasonSummary
)
