package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.SeasonCompetitionConfig

interface CompetitionRepository {
    fun saveSeasonConfig(config: SeasonCompetitionConfig)
    fun getSeasonConfig(competitionId: String, season: Int): SeasonCompetitionConfig?
    fun listSeasonConfigs(season: Int): List<SeasonCompetitionConfig>
}
