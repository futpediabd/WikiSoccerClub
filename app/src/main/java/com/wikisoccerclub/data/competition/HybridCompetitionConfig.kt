package com.wikisoccerclub.data.competition

data class HybridCompetitionConfig(
    val groupStageConfig: GroupStageConfig = GroupStageConfig(
        groupCount = 8,
        teamsPerGroup = 4,
        qualifiedPerGroup = 2,
        homeAndAway = true
    ),
    val knockoutTwoLegged: Boolean = true,
    val finalSingleLeg: Boolean = true
)
