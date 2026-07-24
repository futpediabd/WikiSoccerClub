package com.wikisoccerclub.data.competition

import com.wikisoccerclub.data.match.CompletedMatchResult

object HybridCompetitionEngine {

    fun applyGroupResult(
        progress: HybridCompetitionProgress,
        result: CompletedMatchResult,
        config: HybridCompetitionConfig
    ): HybridCompetitionProgress {
        if (progress.phase != HybridCompetitionPhase.GROUP_STAGE) {
            return progress
        }

        val groupStage = progress.groupStage ?: return progress

        val updatedGroupStage = GroupStageEngine.applyResult(
            progress = groupStage,
            result = result,
            qualifiedPerGroup = config.groupStageConfig.qualifiedPerGroup
        )

        if (!updatedGroupStage.completed) {
            return progress.copy(groupStage = updatedGroupStage)
        }

        val knockout = createKnockout(
            groupStage = updatedGroupStage,
            twoLegged = config.knockoutTwoLegged
        )

        return progress.copy(
            phase = HybridCompetitionPhase.KNOCKOUT,
            groupStage = updatedGroupStage,
            knockout = knockout
        )
    }

    fun applyKnockoutResult(
        progress: HybridCompetitionProgress,
        result: KnockoutMatchResult
    ): HybridCompetitionProgress {
        if (progress.phase != HybridCompetitionPhase.KNOCKOUT) {
            return progress
        }

        val knockout = progress.knockout ?: return progress

        val updatedKnockout = KnockoutEngine.applyResult(
            progress = knockout,
            result = result
        )

        return if (updatedKnockout.completed) {
            progress.copy(
                phase = HybridCompetitionPhase.COMPLETED,
                knockout = updatedKnockout,
                championTeamId = updatedKnockout.championTeamId,
                completed = true
            )
        } else {
            progress.copy(knockout = updatedKnockout)
        }
    }

    private fun createKnockout(
        groupStage: GroupStageProgress,
        twoLegged: Boolean
    ): KnockoutCompetitionProgress {
        val groups = groupStage.groups.sortedBy { it.name }

        require(groups.size == 8) {
            "A transição automática exige 8 grupos."
        }

        val first = groups.associate { group ->
            group.id to group.standings.entries[0].teamId
        }
        val second = groups.associate { group ->
            group.id to group.standings.entries[1].teamId
        }

        val pairings = listOf(
            first.getValue(groups[0].id) to second.getValue(groups[1].id),
            first.getValue(groups[2].id) to second.getValue(groups[3].id),
            first.getValue(groups[4].id) to second.getValue(groups[5].id),
            first.getValue(groups[6].id) to second.getValue(groups[7].id),
            first.getValue(groups[1].id) to second.getValue(groups[0].id),
            first.getValue(groups[3].id) to second.getValue(groups[2].id),
            first.getValue(groups[5].id) to second.getValue(groups[4].id),
            first.getValue(groups[7].id) to second.getValue(groups[6].id)
        )

        val teams = pairings.flatMap { listOf(it.first, it.second) }

        return KnockoutFactory.create(
            competitionId = groupStage.competitionId,
            teamIds = teams,
            initialRound = KnockoutRoundType.ROUND_OF_16,
            twoLegged = twoLegged
        )
    }
}
