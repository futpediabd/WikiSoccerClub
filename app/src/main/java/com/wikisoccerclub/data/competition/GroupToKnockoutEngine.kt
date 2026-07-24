package com.wikisoccerclub.data.competition

object GroupToKnockoutEngine {

    fun createRoundOf16(
        groupStage: GroupStageProgress
    ): KnockoutCompetitionProgress {
        require(groupStage.completed) {
            "A fase de grupos ainda não foi concluída."
        }

        val qualifiedByGroup = groupStage.groups.associate { group ->
            group.id to group.standings.entries
                .take(2)
                .map { it.teamId }
        }

        val orderedGroups = groupStage.groups.sortedBy { it.name }
        require(orderedGroups.size == 8) {
            "As oitavas exigem 8 grupos."
        }

        val pairings = listOf(
            qualifiedByGroup.getValue(orderedGroups[0].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[1].id)[1],
            qualifiedByGroup.getValue(orderedGroups[2].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[3].id)[1],
            qualifiedByGroup.getValue(orderedGroups[4].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[5].id)[1],
            qualifiedByGroup.getValue(orderedGroups[6].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[7].id)[1],
            qualifiedByGroup.getValue(orderedGroups[1].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[0].id)[1],
            qualifiedByGroup.getValue(orderedGroups[3].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[2].id)[1],
            qualifiedByGroup.getValue(orderedGroups[5].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[4].id)[1],
            qualifiedByGroup.getValue(orderedGroups[7].id)[0] to
                qualifiedByGroup.getValue(orderedGroups[6].id)[1]
        )

        val teamIds = pairings.flatMap { listOf(it.first, it.second) }

        return KnockoutFactory.create(
            competitionId = groupStage.competitionId,
            teamIds = teamIds,
            initialRound = KnockoutRoundType.ROUND_OF_16,
            twoLegged = true
        )
    }
}
