package com.wikisoccerclub.data.competition

class CompetitionHistoryRepository {

    private val history = mutableListOf<CompetitionSeasonRecord>()

    fun add(record: CompetitionSeasonRecord) {
        history.removeAll {
            it.competitionId == record.competitionId &&
                it.season == record.season
        }
        history += record
    }

    fun byCompetition(
        competitionId: String
    ): List<CompetitionSeasonRecord> =
        history.filter {
            it.competitionId == competitionId
        }.sortedByDescending {
            it.season
        }

    fun all(): CompetitionHistory =
        CompetitionHistory(
            records = history.sortedWith(
                compareByDescending<CompetitionSeasonRecord> {
                    it.season
                }.thenBy {
                    it.competitionId
                }
            )
        )
}
