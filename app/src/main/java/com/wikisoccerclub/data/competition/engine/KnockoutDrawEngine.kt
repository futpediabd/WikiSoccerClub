package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.DrawClub
import com.wikisoccerclub.data.competition.model.KnockoutPairing
import com.wikisoccerclub.data.competition.model.LiveDrawEvent
import com.wikisoccerclub.data.competition.model.LiveDrawResult
import kotlin.random.Random

class KnockoutDrawEngine(
    private val random: Random = Random.Default
) {
    fun draw(
        clubs: List<DrawClub>,
        twoLegged: Boolean,
        lowerLevelHostsFirstLeg: Boolean
    ): LiveDrawResult<List<KnockoutPairing>> {
        require(clubs.size >= 2 && clubs.size % 2 == 0)

        val shuffled = clubs.shuffled(random).toMutableList()
        val pairings = mutableListOf<KnockoutPairing>()
        val events = mutableListOf<LiveDrawEvent>()
        var pairingNumber = 1
        var order = 1

        while (shuffled.isNotEmpty()) {
            val first = shuffled.removeAt(0)
            val second = shuffled.removeAt(0)

            val firstLegHome = when {
                !lowerLevelHostsFirstLeg -> listOf(first, second).random(random)
                first.level < second.level -> first
                second.level < first.level -> second
                else -> listOf(first, second).random(random)
            }

            val firstLegAway =
                if (firstLegHome.clubId == first.clubId) second else first

            pairings += KnockoutPairing(
                pairingNumber = pairingNumber,
                firstLegHome = firstLegHome,
                firstLegAway = firstLegAway,
                secondLegHome = if (twoLegged) firstLegAway else null,
                secondLegAway = if (twoLegged) firstLegHome else null
            )

            events += LiveDrawEvent(
                order = order++,
                message = "Confronto $pairingNumber: ${firstLegHome.clubName} x ${firstLegAway.clubName}",
                pairingNumber = pairingNumber
            )

            pairingNumber++
        }

        return LiveDrawResult(pairings, events)
    }
}
