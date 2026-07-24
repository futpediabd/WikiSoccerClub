package com.wikisoccerclub.data.board

enum class BoardObjectiveType {
    LEAGUE_POSITION,
    WIN_COMPETITION,
    REACH_STAGE,
    AVOID_RELEGATION,
    DEVELOP_YOUTH,
    REDUCE_WAGE_BILL,
    POSITIVE_BALANCE,
    SIGN_PLAYERS,
    SELL_PLAYERS
}

enum class BoardObjectiveStatus {
    ACTIVE,
    COMPLETED,
    FAILED,
    CANCELLED
}

enum class BoardConfidenceLevel {
    VERY_LOW,
    LOW,
    STABLE,
    HIGH,
    VERY_HIGH
}

data class BoardObjective(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val type: BoardObjectiveType,
    val title: String,
    val description: String,
    val targetValue: Int,
    val currentValue: Int = 0,
    val weight: Int = 50,
    val deadlineDay: Int? = null,
    val status: BoardObjectiveStatus =
        BoardObjectiveStatus.ACTIVE
)

data class ClubReputation(
    val clubId: String,
    val nationalReputation: Int,
    val continentalReputation: Int,
    val globalReputation: Int
)

data class ManagerReputation(
    val managerId: String,
    val reputation: Int,
    val trophiesWon: Int = 0,
    val promotions: Int = 0,
    val relegations: Int = 0,
    val matchesManaged: Int = 0,
    val wins: Int = 0
)

data class BoardEvaluation(
    val clubId: String,
    val seasonYear: Int,
    val confidence: Int,
    val confidenceLevel: BoardConfidenceLevel,
    val completedObjectives: Int,
    val failedObjectives: Int,
    val message: String,
    val dismissalRisk: Boolean
)
