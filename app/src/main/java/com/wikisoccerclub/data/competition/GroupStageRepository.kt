package com.wikisoccerclub.data.competition

class GroupStageRepository {

    private val stages = linkedMapOf<String, GroupStageProgress>()

    fun save(progress: GroupStageProgress) {
        stages[progress.competitionId] = progress
    }

    fun find(competitionId: String): GroupStageProgress? =
        stages[competitionId]

    fun all(): List<GroupStageProgress> =
        stages.values.toList()

    fun remove(competitionId: String) {
        stages.remove(competitionId)
    }
}
