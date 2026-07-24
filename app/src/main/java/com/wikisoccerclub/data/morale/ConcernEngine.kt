package com.wikisoccerclub.data.morale

object ConcernEngine {

    fun detectPlayingTimeConcern(
        morale: PlayerMorale,
        currentDay: Int
    ): PlayerConcern? {
        val difference =
            morale.expectedPlayingTime -
                morale.recentMinutesRate

        if (difference < 25) return null

        return PlayerConcern(
            id = "tempo_${morale.playerId}_$currentDay",
            playerId = morale.playerId,
            type = PlayerConcernType.PLAYING_TIME,
            title = "Pouco tempo de jogo",
            description =
                "O jogador considera que está atuando menos do que esperava.",
            severity = difference.coerceIn(1, 100),
            createdDay = currentDay,
            deadlineDay = currentDay + 30
        )
    }

    fun resolve(
        concern: PlayerConcern,
        fulfilled: Boolean
    ): PlayerConcern {
        if (concern.status != ConcernStatus.ACTIVE) {
            return concern
        }

        return concern.copy(
            status = if (fulfilled) {
                ConcernStatus.RESOLVED
            } else {
                ConcernStatus.BROKEN_PROMISE
            }
        )
    }

    fun checkDeadline(
        concern: PlayerConcern,
        currentDay: Int
    ): PlayerConcern {
        if (
            concern.status != ConcernStatus.ACTIVE ||
            concern.deadlineDay == null ||
            currentDay <= concern.deadlineDay
        ) {
            return concern
        }

        return concern.copy(
            status = ConcernStatus.BROKEN_PROMISE
        )
    }

    fun moraleImpact(
        concern: PlayerConcern
    ): Int = when (concern.status) {
        ConcernStatus.RESOLVED ->
            3 + concern.severity / 20
        ConcernStatus.BROKEN_PROMISE ->
            -(5 + concern.severity / 10)
        ConcernStatus.ACTIVE ->
            -(concern.severity / 25)
    }
}
