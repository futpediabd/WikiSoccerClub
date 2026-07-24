package com.wikisoccerclub.data.match

enum class LiveMatchEventType {
    KICK_OFF,
    SHOT,
    SHOT_ON_TARGET,
    GOAL,
    YELLOW_CARD,
    RED_CARD,
    INJURY,
    SUBSTITUTION,
    HALF_TIME,
    FULL_TIME
}

data class LiveMatchEvent(
    val id: String,
    val minute: Int,
    val type: LiveMatchEventType,
    val teamId: String? = null,
    val playerId: String? = null,
    val description: String
)
