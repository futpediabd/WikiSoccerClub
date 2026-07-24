package com.wikisoccerclub.data.competition

object KnockoutEngine {

    fun applyResult(
        progress: KnockoutCompetitionProgress,
        result: KnockoutMatchResult
    ): KnockoutCompetitionProgress {
        val tieIndex = progress.ties.indexOfFirst { tie ->
            tie.firstLegMatchId == result.matchId ||
                tie.secondLegMatchId == result.matchId
        }

        if (tieIndex < 0) return progress

        val ties = progress.ties.toMutableList()
        val currentTie = ties[tieIndex]

        var updatedTie = when (result.matchId) {
            currentTie.firstLegMatchId -> currentTie.copy(
                firstLegHomeGoals = result.homeGoals,
                firstLegAwayGoals = result.awayGoals,
                penaltiesHome = result.penaltiesHome,
                penaltiesAway = result.penaltiesAway
            )
            currentTie.secondLegMatchId -> currentTie.copy(
                secondLegHomeGoals = result.homeGoals,
                secondLegAwayGoals = result.awayGoals,
                penaltiesHome = result.penaltiesHome,
                penaltiesAway = result.penaltiesAway
            )
            else -> currentTie
        }

        updatedTie = resolveTie(updatedTie)
        ties[tieIndex] = updatedTie

        val currentRoundTies = ties.filter {
            it.round == progress.currentRound
        }

        val roundFinished = currentRoundTies.isNotEmpty() &&
            currentRoundTies.all { it.completed }

        if (!roundFinished) {
            return progress.copy(ties = ties)
        }

        if (progress.currentRound == KnockoutRoundType.FINAL) {
            return progress.copy(
                ties = ties,
                championTeamId = currentRoundTies
                    .firstOrNull()
                    ?.winnerTeamId,
                completed = true
            )
        }

        val nextRound = nextRound(progress.currentRound)
            ?: return progress.copy(ties = ties)

        val winners = currentRoundTies.mapNotNull {
            it.winnerTeamId
        }

        val nextTies = createNextRoundTies(
            competitionId = progress.competitionId,
            round = nextRound,
            winners = winners
        )

        return progress.copy(
            currentRound = nextRound,
            ties = ties + nextTies
        )
    }

    fun resolveTie(tie: KnockoutTie): KnockoutTie {
        val firstHome = tie.firstLegHomeGoals ?: return tie
        val firstAway = tie.firstLegAwayGoals ?: return tie

        if (tie.secondLegMatchId == null) {
            val winner = when {
                firstHome > firstAway -> tie.homeTeamId
                firstAway > firstHome -> tie.awayTeamId
                (tie.penaltiesHome ?: -1) >
                    (tie.penaltiesAway ?: -1) -> tie.homeTeamId
                (tie.penaltiesAway ?: -1) >
                    (tie.penaltiesHome ?: -1) -> tie.awayTeamId
                else -> null
            }

            return tie.copy(
                winnerTeamId = winner,
                completed = winner != null
            )
        }

        val secondHome = tie.secondLegHomeGoals ?: return tie
        val secondAway = tie.secondLegAwayGoals ?: return tie

        val aggregateHome = firstHome + secondAway
        val aggregateAway = firstAway + secondHome

        val winner = when {
            aggregateHome > aggregateAway -> tie.homeTeamId
            aggregateAway > aggregateHome -> tie.awayTeamId
            (tie.penaltiesHome ?: -1) >
                (tie.penaltiesAway ?: -1) -> tie.homeTeamId
            (tie.penaltiesAway ?: -1) >
                (tie.penaltiesHome ?: -1) -> tie.awayTeamId
            else -> null
        }

        return tie.copy(
            winnerTeamId = winner,
            completed = winner != null
        )
    }

    private fun createNextRoundTies(
        competitionId: String,
        round: KnockoutRoundType,
        winners: List<String>
    ): List<KnockoutTie> =
        winners.chunked(2).mapIndexedNotNull { index, pair ->
            if (pair.size < 2) return@mapIndexedNotNull null

            val baseId = "${competitionId}_${round.name}_$index"
            val singleLeg = round == KnockoutRoundType.FINAL

            KnockoutTie(
                id = baseId,
                round = round,
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

    private fun nextRound(
        round: KnockoutRoundType
    ): KnockoutRoundType? =
        when (round) {
            KnockoutRoundType.PRELIMINARY ->
                KnockoutRoundType.ROUND_OF_128
            KnockoutRoundType.ROUND_OF_128 ->
                KnockoutRoundType.ROUND_OF_64
            KnockoutRoundType.ROUND_OF_64 ->
                KnockoutRoundType.ROUND_OF_32
            KnockoutRoundType.ROUND_OF_32 ->
                KnockoutRoundType.ROUND_OF_16
            KnockoutRoundType.ROUND_OF_16 ->
                KnockoutRoundType.QUARTER_FINAL
            KnockoutRoundType.QUARTER_FINAL ->
                KnockoutRoundType.SEMI_FINAL
            KnockoutRoundType.SEMI_FINAL ->
                KnockoutRoundType.FINAL
            KnockoutRoundType.FINAL -> null
        }
}
