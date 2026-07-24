package com.wikisoccerclub.data.match

data class LiveTeamState(
    val teamId: String,
    val teamName: String,
    val players: List<LivePlayerState>,
    val substitutionsUsed: Int = 0
) {
    val starters: List<LivePlayerState>
        get() = players.filter { it.onField && !it.substitutedOut }

    val bench: List<LivePlayerState>
        get() = players.filter { !it.onField && !it.substitutedOut }

    val substitutionsRemaining: Int
        get() = (5 - substitutionsUsed).coerceAtLeast(0)

    val averageEnergy: Int
        get() = starters
            .map { it.energy }
            .takeIf { it.isNotEmpty() }
            ?.average()
            ?.toInt()
            ?: 0
}
