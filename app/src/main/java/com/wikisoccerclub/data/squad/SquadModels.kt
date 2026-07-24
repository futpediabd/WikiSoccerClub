package com.wikisoccerclub.data.squad

data class SquadPlayer(
    val id: String,
    val name: String,
    val position: String,
    val overall: Int,
    val energy: Int = 100,
    val starter: Boolean = false,
    val bench: Boolean = false
)

data class MatchLineup(
    val starters: List<SquadPlayer>,
    val bench: List<SquadPlayer>
) {
    val isValid: Boolean
        get() = starters.size == 11 && bench.size <= 7
}
