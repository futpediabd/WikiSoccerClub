package com.wikisoccerclub.data.competition

object InjuryEngine {
    fun calculate(
        teams: List<CompetitionTeam>,
        matches: List<CompetitionMatch>,
        currentRound: Int
    ): List<InjuryRow> {
        val players = teams.flatMap { it.players }.associateBy { it.id }
        val teamNames = teams.associate { it.id to it.name }

        return matches.asSequence()
            .filter { it.played }
            .flatMap { match ->
                match.events.asSequence()
                    .filter { it.type == MatchEventType.INJURY }
                    .map { event -> match.round to event }
            }
            .mapNotNull { (injuryRound, event) ->
                val player = players[event.playerId] ?: return@mapNotNull null
                val returnRound = injuryRound + event.injuryRounds
                InjuryRow(
                    playerId = player.id,
                    playerName = player.name,
                    teamName = teamNames[player.teamId].orEmpty(),
                    injuryRound = injuryRound,
                    returnRound = returnRound,
                    remainingRounds = (returnRound - currentRound).coerceAtLeast(0)
                )
            }
            .filter { it.remainingRounds > 0 }
            .sortedWith(compareByDescending<InjuryRow> { it.remainingRounds }.thenBy { it.playerName })
            .toList()
    }
}
