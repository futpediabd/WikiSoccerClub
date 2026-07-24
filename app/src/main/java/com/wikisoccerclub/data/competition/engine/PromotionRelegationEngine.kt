package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.ClubSeasonMovement
import com.wikisoccerclub.data.competition.model.LeagueFinalPosition
import com.wikisoccerclub.data.competition.model.PromotionRelegationRule

object PromotionRelegationEngine {

    fun resolve(
        table: List<LeagueFinalPosition>,
        currentCompetitionId: String,
        higherCompetitionId: String?,
        lowerCompetitionId: String?,
        rule: PromotionRelegationRule
    ): List<ClubSeasonMovement> {
        val movements = mutableListOf<ClubSeasonMovement>()
        val ordered = table.sortedBy { it.position }

        rule.promotedPositions?.let { range ->
            requireNotNull(higherCompetitionId) {
                "A divisão superior precisa estar definida."
            }

            ordered
                .filter { it.position in range }
                .forEach {
                    movements += ClubSeasonMovement(
                        clubId = it.clubId,
                        fromCompetitionId = currentCompetitionId,
                        toCompetitionId = higherCompetitionId,
                        reason = "PROMOCAO_DIRETA"
                    )
                }
        }

        rule.relegatedPositions?.let { range ->
            requireNotNull(lowerCompetitionId) {
                "A divisão inferior precisa estar definida."
            }

            ordered
                .filter { it.position in range }
                .forEach {
                    movements += ClubSeasonMovement(
                        clubId = it.clubId,
                        fromCompetitionId = currentCompetitionId,
                        toCompetitionId = lowerCompetitionId,
                        reason = "REBAIXAMENTO_DIRETO"
                    )
                }
        }

        return movements
    }
}
