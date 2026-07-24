package com.wikisoccerclub.data.competition

enum class TacticalStyle {
    DEFENSIVE,
    BALANCED,
    OFFENSIVE
}

data class CompetitionLineup(
    val formation: String,
    val tacticalStyle: TacticalStyle,
    val starters: List<CompetitionPlayer>,
    val substitutes: List<CompetitionPlayer>
) {
    val isValid: Boolean
        get() = starters.size == 11 && substitutes.size <= 7

    fun contains(playerId: String): Boolean =
        starters.any { it.id == playerId } ||
            substitutes.any { it.id == playerId }
}

data class LineupValidation(
    val valid: Boolean,
    val message: String
)
