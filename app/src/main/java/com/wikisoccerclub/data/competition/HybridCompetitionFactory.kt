package com.wikisoccerclub.data.competition

object HybridCompetitionFactory {

    fun create(
        competitionId: String,
        teamIds: List<String>,
        teamNames: Map<String, String>,
        config: HybridCompetitionConfig = HybridCompetitionConfig()
    ): HybridCompetitionProgress {
        val groupStage = GroupStageFactory.create(
            competitionId = competitionId,
            teamIds = teamIds,
            teamNames = teamNames,
            config = config.groupStageConfig
        )

        return HybridCompetitionProgress(
            competitionId = competitionId,
            phase = HybridCompetitionPhase.GROUP_STAGE,
            groupStage = groupStage,
            knockout = null
        )
    }
}
