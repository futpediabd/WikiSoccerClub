package com.wikisoccerclub.data.competition

class CompetitionProgressRepository {

    private val competitions = linkedMapOf<String, CompetitionProgress>()

    fun save(progress: CompetitionProgress) {
        competitions[progress.competitionId] = progress
    }

    fun find(competitionId: String): CompetitionProgress? =
        competitions[competitionId]

    fun all(): List<CompetitionProgress> =
        competitions.values.toList()

    fun remove(competitionId: String) {
        competitions.remove(competitionId)
    }
}
