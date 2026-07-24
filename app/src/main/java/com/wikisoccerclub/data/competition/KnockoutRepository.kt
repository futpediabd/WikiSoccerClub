package com.wikisoccerclub.data.competition

class KnockoutRepository {

    private val competitions =
        linkedMapOf<String, KnockoutCompetitionProgress>()

    fun save(progress: KnockoutCompetitionProgress) {
        competitions[progress.competitionId] = progress
    }

    fun find(
        competitionId: String
    ): KnockoutCompetitionProgress? =
        competitions[competitionId]

    fun all(): List<KnockoutCompetitionProgress> =
        competitions.values.toList()

    fun remove(competitionId: String) {
        competitions.remove(competitionId)
    }
}
