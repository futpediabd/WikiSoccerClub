package com.wikisoccerclub.data.season

import kotlin.random.Random

object PlayerSeasonEngine {

    fun advance(
        players: List<CareerPlayer>,
        newSeasonYear: Int,
        random: Random = Random.Default
    ): Pair<List<CareerPlayer>, List<PlayerSeasonUpdate>> {
        val updates = mutableListOf<PlayerSeasonUpdate>()

        val updatedPlayers = players.map { player ->
            if (player.retired) return@map player

            val newAge = player.age + 1
            val newOverall = calculateOverall(
                player = player,
                newAge = newAge,
                random = random
            )
            val retired = shouldRetire(
                age = newAge,
                overall = newOverall,
                random = random
            )

            updates += PlayerSeasonUpdate(
                playerId = player.id,
                previousAge = player.age,
                newAge = newAge,
                previousOverall = player.overall,
                newOverall = newOverall,
                contractExpired = player.contractUntilYear < newSeasonYear,
                retired = retired
            )

            player.copy(
                age = newAge,
                overall = newOverall,
                retired = retired
            )
        }

        return updatedPlayers to updates
    }

    private fun calculateOverall(
        player: CareerPlayer,
        newAge: Int,
        random: Random
    ): Int {
        val change = when {
            newAge <= 22 && player.overall < player.potential ->
                random.nextInt(0, 3)
            newAge <= 28 && player.overall < player.potential ->
                random.nextInt(0, 2)
            newAge <= 33 -> 0
            else -> -random.nextInt(1, 3)
        }

        return (player.overall + change)
            .coerceIn(1, player.potential.coerceAtLeast(1))
    }

    private fun shouldRetire(
        age: Int,
        overall: Int,
        random: Random
    ): Boolean = when {
        age < 35 -> false
        age >= 40 -> true
        overall <= 45 -> true
        else -> random.nextInt(100) < (age - 34) * 12
    }
}
