package com.wikisoccerclub.data.morale

enum class MoraleLevel {
    VERY_LOW,
    LOW,
    NORMAL,
    HIGH,
    VERY_HIGH
}

enum class PlayerConcernType {
    PLAYING_TIME,
    CONTRACT,
    SALARY,
    TEAM_PERFORMANCE,
    POSITION,
    TRANSFER_REQUEST,
    CAPTAINCY,
    PROMISE
}

enum class ConcernStatus {
    ACTIVE,
    RESOLVED,
    BROKEN_PROMISE
}

data class PlayerMorale(
    val playerId: String,
    val morale: Int = 50,
    val happiness: Int = 50,
    val managerRelationship: Int = 50,
    val squadRelationship: Int = 50,
    val expectedPlayingTime: Int = 50,
    val recentMinutesRate: Int = 50
) {
    val level: MoraleLevel
        get() = when {
            morale >= 85 -> MoraleLevel.VERY_HIGH
            morale >= 70 -> MoraleLevel.HIGH
            morale >= 45 -> MoraleLevel.NORMAL
            morale >= 25 -> MoraleLevel.LOW
            else -> MoraleLevel.VERY_LOW
        }
}

data class PlayerConcern(
    val id: String,
    val playerId: String,
    val type: PlayerConcernType,
    val title: String,
    val description: String,
    val severity: Int,
    val createdDay: Int,
    val deadlineDay: Int? = null,
    val status: ConcernStatus = ConcernStatus.ACTIVE
)

data class SquadAtmosphere(
    val clubId: String,
    val atmosphere: Int,
    val leadership: Int,
    val cohesion: Int,
    val managerSupport: Int,
    val unhappyPlayers: Int
)

data class TeamTalkResult(
    val moraleChange: Int,
    val managerRelationshipChange: Int,
    val message: String
)
