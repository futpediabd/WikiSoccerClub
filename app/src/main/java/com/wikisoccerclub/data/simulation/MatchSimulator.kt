package com.wikisoccerclub.data.simulation

import com.wikisoccerclub.data.squad.MatchLineup
import kotlin.math.max
import kotlin.random.Random

data class MatchResult(
    val homeGoals: Int,
    val awayGoals: Int,
    val homeShots: Int,
    val awayShots: Int,
    val possessionHome: Int,
    val possessionAway: Int,
    val events: List<String>
)

object MatchSimulator {

    fun simulate(lineup: MatchLineup, userIsHome: Boolean): MatchResult {
        val teamAverage = lineup.starters.map { it.overall }.average().takeIf { !it.isNaN() } ?: 60.0
        val strengthBonus = ((teamAverage - 60.0) / 12.0).toInt().coerceIn(-1, 3)

        val userGoals = max(0, Random.nextInt(0, 4) + strengthBonus)
        val opponentGoals = Random.nextInt(0, 4)

        val homeGoals = if (userIsHome) userGoals else opponentGoals
        val awayGoals = if (userIsHome) opponentGoals else userGoals

        val userShots = max(userGoals + 2, Random.nextInt(6, 15) + strengthBonus)
        val opponentShots = max(opponentGoals + 2, Random.nextInt(5, 14))

        val possessionUser = (50 + strengthBonus * 3 + Random.nextInt(-6, 7)).coerceIn(35, 65)
        val possessionHome = if (userIsHome) possessionUser else 100 - possessionUser

        val events = buildList {
            repeat(homeGoals) { add("${Random.nextInt(8, 89)}' Gol do mandante") }
            repeat(awayGoals) { add("${Random.nextInt(8, 89)}' Gol do visitante") }
            add("${Random.nextInt(20, 80)}' Cartão amarelo")
        }.sortedBy { it.substringBefore("'").toIntOrNull() ?: 90 }

        return MatchResult(
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            homeShots = if (userIsHome) userShots else opponentShots,
            awayShots = if (userIsHome) opponentShots else userShots,
            possessionHome = possessionHome,
            possessionAway = 100 - possessionHome,
            events = events
        )
    }
}
