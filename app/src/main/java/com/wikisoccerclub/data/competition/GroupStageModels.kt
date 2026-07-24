package com.wikisoccerclub.data.competition

data class CompetitionGroup(
    val id: String,
    val name: String,
    val teamIds: List<String>,
    val standings: CompetitionStandings
)

data class GroupStageMatch(
    val id: String,
    val groupId: String,
    val round: Int,
    val homeTeamId: String,
    val awayTeamId: String,
    val played: Boolean = false,
    val homeGoals: Int? = null,
    val awayGoals: Int? = null
)

data class GroupStageProgress(
    val competitionId: String,
    val groups: List<CompetitionGroup>,
    val matches: List<GroupStageMatch>,
    val currentRound: Int = 1,
    val qualifiedTeamIds: List<String> = emptyList(),
    val completed: Boolean = false
)
