package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.SeasonCompetitionConfig

class InMemoryCompetitionRepository : CompetitionRepository {
    private val configs = linkedMapOf<String, SeasonCompetitionConfig>()

    override fun saveSeasonConfig(config: SeasonCompetitionConfig) {
        configs["${config.competitionId}::${config.season}"] = config
    }

    override fun getSeasonConfig(
        competitionId: String,
        season: Int
    ): SeasonCompetitionConfig? = configs["$competitionId::$season"]

    override fun listSeasonConfigs(season: Int): List<SeasonCompetitionConfig> =
        configs.values.filter { it.season == season }
}
