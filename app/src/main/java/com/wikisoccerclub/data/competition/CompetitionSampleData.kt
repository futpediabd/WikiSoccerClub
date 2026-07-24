package com.wikisoccerclub.data.competition

object CompetitionSampleData {

    val teams = listOf(
        CompetitionTeam("user", "Exemplo Futebol Clube"),
        CompetitionTeam("atletico", "Atlético Central"),
        CompetitionTeam("uniao", "União Esportiva"),
        CompetitionTeam("nacional", "Nacional da Serra")
    )

    val matches = listOf(
        CompetitionMatch("user", "atletico", 2, 1),
        CompetitionMatch("uniao", "nacional", 0, 0),
        CompetitionMatch("atletico", "uniao", 1, 3),
        CompetitionMatch("nacional", "user", 1, 1)
    )

    fun standings(): List<StandingRow> {
        return StandingsEngine.calculate(
            teams = teams,
            matches = matches
        )
    }
}
