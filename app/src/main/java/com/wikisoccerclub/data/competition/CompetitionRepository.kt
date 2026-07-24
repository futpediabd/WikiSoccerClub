package com.wikisoccerclub.data.competition

class CompetitionRepository(
    private val simulator: CompetitionSimulator = CompetitionSimulator()
) {
    private val teams = baseTeams().map { team ->
        team.copy(
            players = CompetitionSquadFactory.create(team.id, team.name)
        )
    }

    private val matches = RoundRobinScheduler
        .generate(teams, doubleRound = true)
        .toMutableList()

    fun snapshot(): CompetitionSnapshot {
        val round = currentRound()
        val totalRounds = matches.maxOfOrNull { it.round } ?: 0
        val userTeam = teams.firstOrNull { it.userControlled }

        return CompetitionSnapshot(
            name = "Campeonato Estadual",
            currentRound = round,
            totalRounds = totalRounds,
            completed = matches.isNotEmpty() && matches.all { it.played },
            teams = teams,
            matches = matches.toList(),
            standings = StandingsEngine.calculate(teams, matches),
            topScorers = TopScorersEngine.calculate(teams, matches),
            discipline = DisciplineEngine.calculate(teams, matches),
            injuries = InjuryEngine.calculate(
                teams = teams,
                matches = matches,
                currentRound = round
            ),
            userTeamAvailability = userTeam?.let {
                AvailabilityEngine.calculateForTeam(
                    team = it,
                    matches = matches,
                    currentRound = round
                )
            }
        )
    }

    fun currentRoundMatches(): List<CompetitionMatch> =
        matches.filter { it.round == currentRound() }

    fun matchById(matchId: String): CompetitionMatch? =
        matches.firstOrNull { it.id == matchId }

    fun availablePlayersForUserTeam(): List<CompetitionPlayer> =
        snapshot()
            .userTeamAvailability
            ?.availablePlayers
            ?.map { it.player }
            .orEmpty()

    fun simulateCurrentRound() {
        val round = currentRound()
        val teamsById = teams.associateBy { it.id }

        matches.withIndex()
            .filter { (_, match) ->
                match.round == round && !match.played
            }
            .forEach { (index, match) ->
                matches[index] = simulator.simulate(
                    match = match,
                    teamsById = teamsById
                )
            }
    }

    private fun currentRound(): Int =
        matches.firstOrNull { !it.played }?.round
            ?: (matches.maxOfOrNull { it.round } ?: 1)

    private fun baseTeams(): List<CompetitionTeam> = listOf(
        CompetitionTeam("user", "Wiki Soccer Club", 76, true),
        CompetitionTeam("atletico", "Atlético Central", 72),
        CompetitionTeam("uniao", "União Esportiva", 68),
        CompetitionTeam("nacional", "Nacional da Serra", 66),
        CompetitionTeam("ferroviario", "Ferroviário FC", 61),
        CompetitionTeam("olimpico", "Olímpico Clube", 59),
        CompetitionTeam("juventude", "Juventude do Vale", 55),
        CompetitionTeam("independente", "Independente AC", 52)
    )
}
