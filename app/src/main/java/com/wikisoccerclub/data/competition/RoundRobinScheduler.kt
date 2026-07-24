package com.wikisoccerclub.data.competition

object RoundRobinScheduler {

    fun generate(
        teams: List<CompetitionTeam>,
        doubleRound: Boolean = true
    ): List<CompetitionMatch> {
        if (teams.size < 2) return emptyList()

        val participants = teams.map { it.id }.toMutableList()
        if (participants.size % 2 != 0) participants.add(BYE)

        val firstLeg = mutableListOf<CompetitionMatch>()
        val totalRounds = participants.size - 1
        val matchesPerRound = participants.size / 2
        val rotating = participants.toMutableList()

        for (roundIndex in 0 until totalRounds) {
            for (pairIndex in 0 until matchesPerRound) {
                val left = rotating[pairIndex]
                val right = rotating[rotating.lastIndex - pairIndex]

                if (left == BYE || right == BYE) continue

                val swapHome = (roundIndex + pairIndex) % 2 != 0
                val home = if (swapHome) right else left
                val away = if (swapHome) left else right

                firstLeg += CompetitionMatch(
                    id = "r${roundIndex + 1}_${home}_$away",
                    round = roundIndex + 1,
                    homeTeamId = home,
                    awayTeamId = away
                )
            }

            val fixed = rotating.first()
            val last = rotating.removeAt(rotating.lastIndex)
            rotating.add(1, last)
            rotating[0] = fixed
        }

        if (!doubleRound) return firstLeg

        val secondLeg = firstLeg.map { match ->
            match.copy(
                id = "r${match.round + totalRounds}_${match.awayTeamId}_${match.homeTeamId}",
                round = match.round + totalRounds,
                homeTeamId = match.awayTeamId,
                awayTeamId = match.homeTeamId
            )
        }

        return firstLeg + secondLeg
    }

    private const val BYE = "__bye__"
}
