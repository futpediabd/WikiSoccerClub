package com.wikisoccerclub.data.competition

enum class HybridCompetitionPhase {
    GROUP_STAGE,
    KNOCKOUT,
    COMPLETED
}

data class HybridCompetitionProgress(
    val competitionId: String,
    val phase: HybridCompetitionPhase,
    val groupStage: GroupStageProgress?,
    val knockout: KnockoutCompetitionProgress?,
    val championTeamId: String? = null,
    val completed: Boolean = false
)
