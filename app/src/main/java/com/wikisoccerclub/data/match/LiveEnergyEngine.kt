package com.wikisoccerclub.data.match

object LiveEnergyEngine {

    fun updateTeam(
        team: LiveTeamState,
        minute: Int,
        tacticalIntensity: Int = 1
    ): LiveTeamState {
        if (minute <= 0) return team

        val baseLoss = when {
            minute <= 30 -> 1
            minute <= 60 -> 2
            else -> 3
        }

        val totalLoss = (baseLoss + tacticalIntensity).coerceAtLeast(1)

        return team.copy(
            players = team.players.map { state ->
                if (state.onField && !state.substitutedOut) {
                    state.copy(
                        energy = (state.energy - totalLoss)
                            .coerceAtLeast(25)
                    )
                } else {
                    state
                }
            }
        )
    }

    fun performanceMultiplier(energy: Int): Double =
        when {
            energy >= 90 -> 1.08
            energy >= 75 -> 1.03
            energy >= 60 -> 1.00
            energy >= 45 -> 0.94
            else -> 0.86
        }
}
