package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.model.SeasonAdvanceRequest
import com.wikisoccerclub.data.competition.model.SeasonTransitionResult

class SeasonAdvanceCoordinator(
    private val seasonTransitionService: SeasonTransitionService
) {

    fun advance(
        request: SeasonAdvanceRequest
    ): SeasonTransitionResult {
        require(request.season > 0) {
            "A temporada atual é inválida."
        }

        return seasonTransitionService.closeSeason(
            season = request.season,
            leagueTables = request.leagueTables,
            divisionLinks = request.divisionLinks,
            rules = request.promotionRelegationRules,
            qualificationSources = request.qualificationSources
        )
    }
}
