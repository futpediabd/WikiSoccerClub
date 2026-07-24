package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.NextSeasonQualificationEngine
import com.wikisoccerclub.data.competition.engine.PromotionRelegationEngine
import com.wikisoccerclub.data.competition.engine.QualificationSource
import com.wikisoccerclub.data.competition.model.LeagueFinalPosition
import com.wikisoccerclub.data.competition.model.PromotionRelegationRule
import com.wikisoccerclub.data.competition.model.SeasonTransitionResult
import com.wikisoccerclub.data.competition.repository.SeasonTransitionRepository

class SeasonTransitionService(
    private val repository: SeasonTransitionRepository
) {

    fun closeSeason(
        season: Int,
        leagueTables: Map<String, List<LeagueFinalPosition>>,
        divisionLinks: Map<String, Pair<String?, String?>>,
        rules: Map<String, PromotionRelegationRule>,
        qualificationSources: List<QualificationSource>
    ): SeasonTransitionResult {
        val movements = mutableListOf<
            com.wikisoccerclub.data.competition.model.ClubSeasonMovement
        >()
        val warnings = mutableListOf<String>()

        rules.forEach { (competitionId, rule) ->
            val table = leagueTables[competitionId]

            if (table == null) {
                warnings += "Tabela não encontrada para $competitionId"
                return@forEach
            }

            val links = divisionLinks[competitionId]
            val higher = links?.first
            val lower = links?.second

            movements += PromotionRelegationEngine.resolve(
                table = table,
                currentCompetitionId = competitionId,
                higherCompetitionId = higher,
                lowerCompetitionId = lower,
                rule = rule
            )
        }

        val result = SeasonTransitionResult(
            seasonFinished = season,
            nextSeason = season + 1,
            movements = movements,
            qualifiedByCompetition =
                NextSeasonQualificationEngine.resolve(qualificationSources),
            warnings = warnings
        )

        repository.save(result)
        return result
    }
}
