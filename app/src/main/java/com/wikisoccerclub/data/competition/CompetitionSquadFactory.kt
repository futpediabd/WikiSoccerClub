package com.wikisoccerclub.data.competition

object CompetitionSquadFactory {

    fun create(teamId: String, teamName: String): List<CompetitionPlayer> {
        val prefix = teamName
            .split(" ")
            .firstOrNull()
            .orEmpty()
            .take(4)

        return listOf(
            CompetitionPlayer("${teamId}_gk", teamId, "$prefix Goleiro", "GOL", 20),
            CompetitionPlayer("${teamId}_rb", teamId, "$prefix Lateral", "LD", 38),
            CompetitionPlayer("${teamId}_cb1", teamId, "$prefix Zagueiro I", "ZAG", 34),
            CompetitionPlayer("${teamId}_cb2", teamId, "$prefix Zagueiro II", "ZAG", 35),
            CompetitionPlayer("${teamId}_lb", teamId, "$prefix Ala", "LE", 42),
            CompetitionPlayer("${teamId}_dm", teamId, "$prefix Volante", "VOL", 46),
            CompetitionPlayer("${teamId}_cm1", teamId, "$prefix Meia I", "MC", 58),
            CompetitionPlayer("${teamId}_cm2", teamId, "$prefix Meia II", "MC", 61),
            CompetitionPlayer("${teamId}_rw", teamId, "$prefix Ponta D", "PD", 69),
            CompetitionPlayer("${teamId}_st", teamId, "$prefix Centroavante", "ATA", 78),
            CompetitionPlayer("${teamId}_lw", teamId, "$prefix Ponta E", "PE", 71)
        )
    }
}
