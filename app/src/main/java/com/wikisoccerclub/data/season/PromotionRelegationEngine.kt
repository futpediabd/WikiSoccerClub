package com.wikisoccerclub.data.season

import com.wikisoccerclub.data.competition.CompetitionOutcome

object PromotionRelegationEngine {

    fun apply(
        divisions: List<LeagueDivisionState>,
        outcomes: Map<String, CompetitionOutcome>
    ): Pair<List<LeagueDivisionState>, List<PromotionRelegationMovement>> {
        if (divisions.isEmpty()) return emptyList<LeagueDivisionState>() to emptyList()

        val ordered = divisions.sortedBy { it.divisionLevel }
        val updatedTeams = ordered.associate {
            it.competitionId to it.teamIds.toMutableList()
        }.toMutableMap()

        val movements = mutableListOf<PromotionRelegationMovement>()

        for (index in ordered.indices) {
            val division = ordered[index]
            val outcome = outcomes[division.competitionId] ?: continue

            if (index > 0) {
                val upper = ordered[index - 1]
                outcome.promotedTeamIds.forEach { teamId ->
                    if (updatedTeams[division.competitionId]?.remove(teamId) == true) {
                        updatedTeams.getValue(upper.competitionId).add(teamId)
                        movements += PromotionRelegationMovement(
                            teamId = teamId,
                            fromCompetitionId = division.competitionId,
                            toCompetitionId = upper.competitionId,
                            type = MovementType.PROMOTION
                        )
                    }
                }
            }

            if (index < ordered.lastIndex) {
                val lower = ordered[index + 1]
                outcome.relegatedTeamIds.forEach { teamId ->
                    if (updatedTeams[division.competitionId]?.remove(teamId) == true) {
                        updatedTeams.getValue(lower.competitionId).add(teamId)
                        movements += PromotionRelegationMovement(
                            teamId = teamId,
                            fromCompetitionId = division.competitionId,
                            toCompetitionId = lower.competitionId,
                            type = MovementType.RELEGATION
                        )
                    }
                }
            }
        }

        val updatedDivisions = ordered.map { division ->
            division.copy(
                teamIds = updatedTeams
                    .getValue(division.competitionId)
                    .distinct()
            )
        }

        validateNoDuplicateTeams(updatedDivisions)
        return updatedDivisions to movements
    }

    private fun validateNoDuplicateTeams(
        divisions: List<LeagueDivisionState>
    ) {
        val allTeams = divisions.flatMap { it.teamIds }
        require(allTeams.size == allTeams.distinct().size) {
            "Um mesmo time não pode permanecer em duas divisões."
        }
    }
}
