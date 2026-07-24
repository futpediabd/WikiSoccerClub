package com.wikisoccerclub.data.competition

object TopScorersEngine {
    fun calculate(
        teams: List<CompetitionTeam>,
        matches: List<CompetitionMatch>
    ): List<TopScorerRow> {
        val players = teams.flatMap { it.players }.associateBy { it.id }
        val teamNames = teams.associate { it.id to it.name }

        return matches.asSequence()
            .flatMap { it.events.asSequence() }
            .filter { it.type == MatchEventType.GOAL }
            .groupingBy { it.playerId }
            .eachCount()
            .mapNotNull { (playerId, goals) ->
                val player = players[playerId] ?: return@mapNotNull null
                TopScorerRow(
                    position = 0,
                    playerId = playerId,
                    playerName = player.name,
                    teamName = teamNames[player.teamId].orEmpty(),
                    goals = goals
                )
            }
            .sortedWith(compareByDescending<TopScorerRow> { it.goals }
                .thenBy { it.playerName })
            .mapIndexed { index, row -> row.copy(position = index + 1) }
    }
}
