package com.wikisoccerclub.data.competition

object DisciplineEngine {
    private const val YELLOW_LIMIT = 3

    fun calculate(
        teams: List<CompetitionTeam>,
        matches: List<CompetitionMatch>
    ): List<DisciplineRow> {
        val players = teams.flatMap { it.players }.associateBy { it.id }
        val teamNames = teams.associate { it.id to it.name }

        return matches.asSequence()
            .filter { it.played }
            .flatMap { it.events.asSequence() }
            .filter {
                it.type == MatchEventType.YELLOW_CARD ||
                    it.type == MatchEventType.RED_CARD
            }
            .groupBy { it.playerId }
            .mapNotNull { (playerId, events) ->
                val player = players[playerId] ?: return@mapNotNull null
                val yellows = events.count { it.type == MatchEventType.YELLOW_CARD }
                val reds = events.count { it.type == MatchEventType.RED_CARD }
                DisciplineRow(
                    position = 0,
                    playerId = player.id,
                    playerName = player.name,
                    teamName = teamNames[player.teamId].orEmpty(),
                    yellowCards = yellows,
                    redCards = reds,
                    suspensionRounds = yellows / YELLOW_LIMIT + reds
                )
            }
            .sortedWith(
                compareByDescending<DisciplineRow> { it.redCards }
                    .thenByDescending { it.yellowCards }
                    .thenBy { it.playerName }
            )
            .mapIndexed { index, row -> row.copy(position = index + 1) }
    }
}
