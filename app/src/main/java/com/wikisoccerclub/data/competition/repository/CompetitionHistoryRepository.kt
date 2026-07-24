package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.ClubCompetitionHistory
import com.wikisoccerclub.data.competition.model.CompetitionSeasonHistory

interface CompetitionHistoryRepository {
    fun saveSeasonHistory(history: CompetitionSeasonHistory)
    fun listCompetitionHistory(competitionId: String): List<CompetitionSeasonHistory>
    fun getClubHistory(clubId: String, competitionId: String): ClubCompetitionHistory?
    fun saveClubHistory(history: ClubCompetitionHistory)
}
