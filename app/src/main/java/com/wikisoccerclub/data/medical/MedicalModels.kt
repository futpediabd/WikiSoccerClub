package com.wikisoccerclub.data.medical

enum class InjurySeverity {
    MINOR,
    MODERATE,
    SERIOUS,
    SEVERE
}

enum class InjuryStatus {
    ACTIVE,
    RECOVERING,
    CLEARED
}

enum class SuspensionReason {
    YELLOW_CARD_ACCUMULATION,
    RED_CARD,
    DISCIPLINARY_DECISION
}

data class PlayerMedicalProfile(
    val playerId: String,
    val injuryProneness: Int,
    val physicalCondition: Int,
    val fatigue: Int,
    val recoveryRate: Int
)

data class Injury(
    val id: String,
    val playerId: String,
    val description: String,
    val severity: InjurySeverity,
    val startSeasonYear: Int,
    val startDay: Int,
    val expectedRecoveryDays: Int,
    val elapsedRecoveryDays: Int = 0,
    val status: InjuryStatus = InjuryStatus.ACTIVE
) {
    val remainingDays: Int
        get() = (
            expectedRecoveryDays - elapsedRecoveryDays
            ).coerceAtLeast(0)
}

data class PlayerDiscipline(
    val playerId: String,
    val competitionId: String,
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val suspensionMatchesRemaining: Int = 0
)

data class Suspension(
    val id: String,
    val playerId: String,
    val competitionId: String,
    val reason: SuspensionReason,
    val totalMatches: Int,
    val matchesServed: Int = 0
) {
    val remainingMatches: Int
        get() = (totalMatches - matchesServed)
            .coerceAtLeast(0)

    val isActive: Boolean
        get() = remainingMatches > 0
}
