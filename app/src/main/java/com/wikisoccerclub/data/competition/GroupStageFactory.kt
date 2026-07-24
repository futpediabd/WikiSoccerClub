package com.wikisoccerclub.data.competition

object GroupStageFactory {

    fun create(
        competitionId: String,
        teamIds: List<String>,
        teamNames: Map<String, String>,
        config: GroupStageConfig = GroupStageConfig()
    ): GroupStageProgress {
        require(teamIds.size == config.requiredTeams) {
            "A competição exige ${config.requiredTeams} times."
        }
        require(teamIds.distinct().size == teamIds.size) {
            "Não pode haver times duplicados."
        }

        val distributed = distribute(teamIds, config.groupCount)

        val groups = distributed.mapIndexed { index, teams ->
            val groupId = "${competitionId}_grupo_${index + 1}"
            CompetitionGroup(
                id = groupId,
                name = "Grupo ${('A'.code + index).toChar()}",
                teamIds = teams,
                standings = CompetitionStandings(
                    competitionId = groupId,
                    entries = teams.map { teamId ->
                        StandingEntry(
                            teamId = teamId,
                            teamName = teamNames[teamId] ?: teamId
                        )
                    }
                )
            )
        }

        val matches = groups.flatMap { group ->
            createRoundRobinMatches(
                competitionId = competitionId,
                group = group,
                homeAndAway = config.homeAndAway
            )
        }

        return GroupStageProgress(
            competitionId = competitionId,
            groups = groups,
            matches = matches
        )
    }

    private fun distribute(
        teamIds: List<String>,
        groupCount: Int
    ): List<List<String>> {
        val groups = List(groupCount) { mutableListOf<String>() }

        teamIds.forEachIndexed { index, teamId ->
            val cycle = index / groupCount
            val position = index % groupCount
            val target = if (cycle % 2 == 0) {
                position
            } else {
                groupCount - 1 - position
            }
            groups[target] += teamId
        }

        return groups
    }

    private fun createRoundRobinMatches(
        competitionId: String,
        group: CompetitionGroup,
        homeAndAway: Boolean
    ): List<GroupStageMatch> {
        val teams = group.teamIds.toMutableList()

        if (teams.size % 2 != 0) {
            teams += "BYE"
        }

        val roundsPerTurn = teams.size - 1
        val half = teams.size / 2
        val matches = mutableListOf<GroupStageMatch>()
        var rotation = teams.toList()

        repeat(roundsPerTurn) { roundIndex ->
            repeat(half) { pairing ->
                val home = rotation[pairing]
                val away = rotation[rotation.lastIndex - pairing]

                if (home != "BYE" && away != "BYE") {
                    val baseId = "${competitionId}_${group.id}_${roundIndex + 1}_$pairing"
                    matches += GroupStageMatch(
                        id = "${baseId}_ida",
                        groupId = group.id,
                        round = roundIndex + 1,
                        homeTeamId = home,
                        awayTeamId = away
                    )

                    if (homeAndAway) {
                        matches += GroupStageMatch(
                            id = "${baseId}_volta",
                            groupId = group.id,
                            round = roundIndex + 1 + roundsPerTurn,
                            homeTeamId = away,
                            awayTeamId = home
                        )
                    }
                }
            }

            rotation = listOf(
                rotation.first(),
                rotation.last()
            ) + rotation.subList(1, rotation.lastIndex)
        }

        return matches.sortedWith(
            compareBy<GroupStageMatch> { it.round }
                .thenBy { it.groupId }
                .thenBy { it.id }
        )
    }
}
