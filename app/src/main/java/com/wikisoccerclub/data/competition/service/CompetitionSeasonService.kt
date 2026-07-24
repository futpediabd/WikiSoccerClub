package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionFormatEngine
import com.wikisoccerclub.data.competition.model.SeasonCompetitionConfig
import com.wikisoccerclub.data.competition.repository.CompetitionRepository

class CompetitionSeasonService(
    private val repository: CompetitionRepository
) {
    fun createSeason(
        season: Int,
        competitionId: String,
        participantClubIds: List<String>
    ): SeasonCompetitionConfig {
        val uniqueIds = participantClubIds.distinct()
        CompetitionFormatEngine.generate(uniqueIds.size)

        return SeasonCompetitionConfig(
            season = season,
            competitionId = competitionId,
            participantClubIds = uniqueIds
        ).also(repository::saveSeasonConfig)
    }
}
