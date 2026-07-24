package com.wikisoccerclub.data.competition

enum class PlayerAvailabilityStatus {
    AVAILABLE,
    SUSPENDED,
    INJURED
}

data class PlayerAvailability(
    val player: CompetitionPlayer,
    val teamName: String,
    val status: PlayerAvailabilityStatus,
    val reason: String,
    val unavailableUntilRound: Int? = null
)

data class TeamAvailability(
    val team: CompetitionTeam,
    val players: List<PlayerAvailability>
) {
    val availablePlayers: List<PlayerAvailability>
        get() = players.filter {
            it.status == PlayerAvailabilityStatus.AVAILABLE
        }

    val unavailablePlayers: List<PlayerAvailability>
        get() = players.filter {
            it.status != PlayerAvailabilityStatus.AVAILABLE
        }
}
