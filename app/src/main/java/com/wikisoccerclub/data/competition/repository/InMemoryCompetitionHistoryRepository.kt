package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.ClubCompetitionHistory
import com.wikisoccerclub.data.competition.model.CompetitionSeasonHistory

class InMemoryCompetitionHistoryRepository : CompetitionHistoryRepository {

    private val seasonHistory = mutableListOf<CompetitionSeasonHistory>()
    private val clubHistory = linkedMapOf<String, ClubCompetitionHistory>()

    override fun saveSeasonHistory(history: CompetitionSeasonHistory) {
        seasonHistory.removeAll {
            it.competitionId == history.competitionId &&
                it.season == history.season
        }
        seasonHistory += history
    }

    override fun listCompetitionHistory(
        competitionId: String
    ): List<CompetitionSeasonHistory> =
        seasonHistory
            .filter { it.competitionId == competitionId }
            .sortedByDescending { it.season }

    override fun getClubHistory(
        clubId: String,
        competitionId: String
    ): ClubCompetitionHistory? =
        clubHistory[key(clubId, competitionId)]

    override fun saveClubHistory(history: ClubCompetitionHistory) {
        clubHistory[key(history.clubId, history.competitionId)] = history
    }

    private fun key(clubId: String, competitionId: String): String =
        "$clubId::$competitionId"
}
