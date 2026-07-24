package com.wikisoccerclub.data.competition

data class GroupStageConfig(
    val groupCount: Int = 4,
    val teamsPerGroup: Int = 4,
    val qualifiedPerGroup: Int = 2,
    val homeAndAway: Boolean = true
) {
    init {
        require(groupCount > 0)
        require(teamsPerGroup >= 2)
        require(qualifiedPerGroup in 1 until teamsPerGroup)
    }

    val requiredTeams: Int
        get() = groupCount * teamsPerGroup
}
