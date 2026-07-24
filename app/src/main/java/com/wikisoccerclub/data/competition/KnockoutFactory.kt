package com.wikisoccerclub.data.competition

object KnockoutFactory {

    fun create(
        competitionId: String,
        teamIds: List<String>,
        initialRound: KnockoutRoundType,
        twoLegged: Boolean = true
    ): KnockoutCompetitionProgress {
        require(teamIds.size >= 2) {
            "São necessários pelo menos dois times."
        }
        require(teamIds.size % 2 == 0) {
            "A quantidade de times precisa ser par."
        }

        val ties = teamIds.chunked(2).mapIndexed { index, pair ->
            val baseId = "${competitionId}_${initialRound.name}_$index"
            val singleLeg = initialRound == KnockoutRoundType.FINAL ||
                !twoLegged

            KnockoutTie(
                id = baseId,
                round = initialRound,
                homeTeamId = pair[0],
                awayTeamId = pair[1],
                firstLegMatchId = "${baseId}_ida",
                secondLegMatchId = if (singleLeg) {
                    null
                } else {
                    "${baseId}_volta"
                }
            )
        }

        return KnockoutCompetitionProgress(
            competitionId = competitionId,
            currentRound = initialRound,
            ties = ties
        )
    }
}
