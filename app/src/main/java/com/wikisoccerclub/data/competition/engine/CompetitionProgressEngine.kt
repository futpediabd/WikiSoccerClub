package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionMatchResult
import com.wikisoccerclub.data.competition.model.CompetitionProgressSnapshot
import com.wikisoccerclub.data.competition.model.KnockoutQualification

object CompetitionProgressEngine {

    fun resolveKnockoutPhase(
        phase: String,
        pairings: List<Pair<String, String>>,
        matches: List<CompetitionMatchResult>,
        twoLegged: Boolean
    ): KnockoutQualification {
        val qualified = mutableListOf<String>()
        val eliminated = mutableListOf<String>()

        pairings.forEach { (firstClubId, secondClubId) ->
            val tieMatches = matches.filter {
                setOf(it.homeClubId, it.awayClubId) ==
                    setOf(firstClubId, secondClubId)
            }.sortedBy { it.leg }

            val winner = if (twoLegged) {
                require(tieMatches.size == 2) {
                    "O confronto de ida e volta precisa ter dois jogos."
                }

                val firstLeg = tieMatches[0]
                val secondLeg = tieMatches[1]

                KnockoutResultEngine.resolveTwoLegTie(
                    firstClubId = firstLeg.homeClubId,
                    secondClubId = firstLeg.awayClubId,
                    firstLeg = MatchScore(
                        homeGoals = firstLeg.homeGoals,
                        awayGoals = firstLeg.awayGoals
                    ),
                    secondLeg = MatchScore(
                        homeGoals = secondLeg.homeGoals,
                        awayGoals = secondLeg.awayGoals,
                        homePenaltyGoals = secondLeg.homePenaltyGoals,
                        awayPenaltyGoals = secondLeg.awayPenaltyGoals
                    )
                ).qualifiedClubId
            } else {
                require(tieMatches.size == 1) {
                    "O confronto em jogo único precisa ter um jogo."
                }

                val match = tieMatches.first()

                KnockoutResultEngine.resolveSingleMatch(
                    homeClubId = match.homeClubId,
                    awayClubId = match.awayClubId,
                    score = MatchScore(
                        homeGoals = match.homeGoals,
                        awayGoals = match.awayGoals,
                        homePenaltyGoals = match.homePenaltyGoals,
                        awayPenaltyGoals = match.awayPenaltyGoals
                    )
                )
            }

            qualified += winner
            eliminated += if (winner == firstClubId) secondClubId else firstClubId
        }

        return KnockoutQualification(
            phase = phase,
            qualifiedClubIds = qualified,
            eliminatedClubIds = eliminated
        )
    }

    fun finishCompetition(
        snapshot: CompetitionProgressSnapshot,
        championClubId: String,
        runnerUpClubId: String
    ): CompetitionProgressSnapshot =
        snapshot.copy(
            currentPhase = "ENCERRADA",
            activeClubIds = listOf(championClubId),
            championClubId = championClubId,
            runnerUpClubId = runnerUpClubId
        )
}
