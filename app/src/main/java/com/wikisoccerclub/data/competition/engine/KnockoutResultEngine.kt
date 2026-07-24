package com.wikisoccerclub.data.competition.engine

data class MatchScore(
    val homeGoals: Int,
    val awayGoals: Int,
    val homePenaltyGoals: Int? = null,
    val awayPenaltyGoals: Int? = null
)

data class TwoLegTieResult(
    val firstClubId: String,
    val secondClubId: String,
    val firstLeg: MatchScore,
    val secondLeg: MatchScore,
    val qualifiedClubId: String,
    val decidedOnPenalties: Boolean
)

object KnockoutResultEngine {

    fun resolveTwoLegTie(
        firstClubId: String,
        secondClubId: String,
        firstLeg: MatchScore,
        secondLeg: MatchScore
    ): TwoLegTieResult {
        val firstClubAggregate =
            firstLeg.homeGoals + secondLeg.awayGoals
        val secondClubAggregate =
            firstLeg.awayGoals + secondLeg.homeGoals

        val qualified = when {
            firstClubAggregate > secondClubAggregate -> firstClubId
            secondClubAggregate > firstClubAggregate -> secondClubId
            else -> resolvePenalties(firstClubId, secondClubId, secondLeg)
        }

        return TwoLegTieResult(
            firstClubId = firstClubId,
            secondClubId = secondClubId,
            firstLeg = firstLeg,
            secondLeg = secondLeg,
            qualifiedClubId = qualified,
            decidedOnPenalties = firstClubAggregate == secondClubAggregate
        )
    }

    fun resolveSingleMatch(
        homeClubId: String,
        awayClubId: String,
        score: MatchScore
    ): String {
        return when {
            score.homeGoals > score.awayGoals -> homeClubId
            score.awayGoals > score.homeGoals -> awayClubId
            else -> resolvePenalties(homeClubId, awayClubId, score)
        }
    }

    private fun resolvePenalties(
        firstClubId: String,
        secondClubId: String,
        score: MatchScore
    ): String {
        val firstPenalties = score.homePenaltyGoals
            ?: error("Pênaltis obrigatórios em caso de empate.")
        val secondPenalties = score.awayPenaltyGoals
            ?: error("Pênaltis obrigatórios em caso de empate.")

        require(firstPenalties != secondPenalties) {
            "A disputa de pênaltis precisa ter um vencedor."
        }

        return if (firstPenalties > secondPenalties) firstClubId else secondClubId
    }
}
