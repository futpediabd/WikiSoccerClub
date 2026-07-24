package com.wikisoccerclub.data.competition

object LineupEngine {
    fun automaticLineup(
        availability: TeamAvailability?,
        tacticalStyle: TacticalStyle = TacticalStyle.BALANCED
    ): CompetitionLineup {
        val available = availability?.availablePlayers?.map { it.player }.orEmpty()
        val starters = mutableListOf<CompetitionPlayer>()

        starters += takeByPosition(available, listOf("GOL"), 1)
        starters += takeByPosition(available, listOf("LD", "LE"), 2)
        starters += takeByPosition(available, listOf("ZAG"), 2)
        starters += takeByPosition(available, listOf("VOL", "MC"), 3)
        starters += takeByPosition(available, listOf("PD", "PE", "ATA"), 3)

        if (starters.size < 11) {
            starters += available
                .filterNot { p -> starters.any { it.id == p.id } }
                .sortedByDescending { score(it, tacticalStyle) }
                .take(11 - starters.size)
        }

        val substitutes = available
            .filterNot { p -> starters.any { it.id == p.id } }
            .sortedByDescending { score(it, tacticalStyle) }
            .take(7)

        return CompetitionLineup(
            formation = "4-3-3",
            tacticalStyle = tacticalStyle,
            starters = starters.take(11),
            substitutes = substitutes
        )
    }

    private fun takeByPosition(
        players: List<CompetitionPlayer>,
        positions: List<String>,
        amount: Int
    ) = players
        .filter { it.position in positions }
        .sortedByDescending { it.finishing + it.fitness }
        .take(amount)

    private fun score(player: CompetitionPlayer, style: TacticalStyle): Int {
        val base = player.finishing + player.fitness + player.discipline
        return when (style) {
            TacticalStyle.DEFENSIVE -> base + if (player.position in listOf("GOL", "ZAG", "VOL")) 25 else 0
            TacticalStyle.BALANCED -> base
            TacticalStyle.OFFENSIVE -> base + if (player.position in listOf("MC", "PD", "PE", "ATA")) 25 else 0
        }
    }
}
