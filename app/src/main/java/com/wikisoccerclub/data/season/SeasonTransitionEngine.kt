package com.wikisoccerclub.data.season

import com.wikisoccerclub.data.competition.CompetitionOutcome

object SeasonTransitionEngine {

    fun execute(
        currentSeason: Season,
        divisions: List<LeagueDivisionState>,
        outcomes: Map<String, CompetitionOutcome>,
        champions: Map<String, String>
    ): SeasonTransitionResult {
        require(currentSeason.finished) {
            "A temporada atual precisa estar encerrada."
        }

        val (updatedDivisions, movements) =
            PromotionRelegationEngine.apply(
                divisions = divisions,
                outcomes = outcomes
            )

        val summary = SeasonSummary(
            year = currentSeason.year,
            champions = champions
        )

        return SeasonTransitionResult(
            previousYear = currentSeason.year,
            newYear = currentSeason.year + 1,
            updatedDivisions = updatedDivisions,
            movements = movements,
            archivedSummary = summary
        )
    }
}
