package com.wikisoccerclub.data.medical

object DisciplineEngine {

    fun registerYellowCard(
        discipline: PlayerDiscipline,
        accumulationLimit: Int = 3
    ): Pair<PlayerDiscipline, Suspension?> {
        require(accumulationLimit >= 2) {
            "O limite de cartões deve ser pelo menos 2."
        }

        val updatedCards = discipline.yellowCards + 1

        return if (updatedCards >= accumulationLimit) {
            val suspension = Suspension(
                id = "susp_amarelo_${discipline.playerId}_" +
                    discipline.competitionId,
                playerId = discipline.playerId,
                competitionId = discipline.competitionId,
                reason =
                    SuspensionReason.YELLOW_CARD_ACCUMULATION,
                totalMatches = 1
            )

            discipline.copy(
                yellowCards = 0,
                suspensionMatchesRemaining =
                    discipline.suspensionMatchesRemaining + 1
            ) to suspension
        } else {
            discipline.copy(
                yellowCards = updatedCards
            ) to null
        }
    }

    fun registerRedCard(
        discipline: PlayerDiscipline,
        suspensionMatches: Int = 1
    ): Pair<PlayerDiscipline, Suspension> {
        require(suspensionMatches >= 1) {
            "A suspensão deve ter pelo menos uma partida."
        }

        val suspension = Suspension(
            id = "susp_vermelho_${discipline.playerId}_" +
                "${discipline.competitionId}_" +
                (discipline.redCards + 1),
            playerId = discipline.playerId,
            competitionId = discipline.competitionId,
            reason = SuspensionReason.RED_CARD,
            totalMatches = suspensionMatches
        )

        return discipline.copy(
            redCards = discipline.redCards + 1,
            suspensionMatchesRemaining =
                discipline.suspensionMatchesRemaining +
                    suspensionMatches
        ) to suspension
    }

    fun serveSuspensionMatch(
        discipline: PlayerDiscipline,
        suspension: Suspension
    ): Pair<PlayerDiscipline, Suspension> {
        if (!suspension.isActive) {
            return discipline to suspension
        }

        val updatedSuspension = suspension.copy(
            matchesServed =
                suspension.matchesServed + 1
        )

        val updatedDiscipline = discipline.copy(
            suspensionMatchesRemaining =
                (
                    discipline.suspensionMatchesRemaining - 1
                    ).coerceAtLeast(0)
        )

        return updatedDiscipline to updatedSuspension
    }

    fun isSuspended(
        discipline: PlayerDiscipline
    ): Boolean =
        discipline.suspensionMatchesRemaining > 0

    fun resetCompetition(
        discipline: PlayerDiscipline
    ): PlayerDiscipline =
        discipline.copy(
            yellowCards = 0,
            redCards = 0,
            suspensionMatchesRemaining = 0
        )
}
