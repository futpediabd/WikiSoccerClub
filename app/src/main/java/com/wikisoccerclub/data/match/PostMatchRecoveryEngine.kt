package com.wikisoccerclub.data.match

object PostMatchRecoveryEngine {

    fun recoverEnergy(
        currentEnergy: Int,
        daysUntilNextMatch: Int
    ): Int {
        val recovery = daysUntilNextMatch.coerceAtLeast(0) * 7
        return (currentEnergy + recovery).coerceAtMost(100)
    }

    fun applyToTeam(
        team: LiveTeamState,
        daysUntilNextMatch: Int
    ): LiveTeamState =
        team.copy(
            players = team.players.map { state ->
                state.copy(
                    energy = recoverEnergy(
                        currentEnergy = state.energy,
                        daysUntilNextMatch = daysUntilNextMatch
                    ),
                    onField = false,
                    substitutedOut = false
                )
            },
            substitutionsUsed = 0
        )
}
