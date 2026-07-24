package com.wikisoccerclub.data.competition

class HybridCompetitionRepository {

    private val competitions =
        linkedMapOf<String, HybridCompetitionProgress>()

    fun save(progress: HybridCompetitionProgress) {
        competitions[progress.competitionId] = progress
    }

    fun find(
        competitionId: String
    ): HybridCompetitionProgress? =
        competitions[competitionId]

    fun all(): List<HybridCompetitionProgress> =
        competitions.values.toList()

    fun remove(competitionId: String) {
        competitions.remove(competitionId)
    }

    fun clear() {
        competitions.clear()
    }
}
