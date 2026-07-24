package com.wikisoccerclub.data.morale

object MoraleEngine {

    fun afterMatch(
        morale: PlayerMorale,
        won: Boolean,
        drew: Boolean,
        startedMatch: Boolean,
        minutesPlayed: Int,
        playerRating: Double
    ): PlayerMorale {
        require(minutesPlayed in 0..130) {
            "Os minutos jogados são inválidos."
        }
        require(playerRating in 0.0..10.0) {
            "A nota deve ficar entre 0 e 10."
        }

        var moraleChange = when {
            won -> 5
            drew -> 1
            else -> -4
        }

        moraleChange += when {
            playerRating >= 8.0 -> 4
            playerRating >= 7.0 -> 2
            playerRating < 5.5 -> -3
            else -> 0
        }

        if (!startedMatch && minutesPlayed == 0) {
            moraleChange -= 3
        }

        val minutesRate = (
            morale.recentMinutesRate * 3 +
                (minutesPlayed * 100 / 90)
                    .coerceIn(0, 100)
            ) / 4

        val playingTimeDifference =
            minutesRate - morale.expectedPlayingTime

        val happinessChange = when {
            playingTimeDifference >= 20 -> 3
            playingTimeDifference <= -30 -> -5
            playingTimeDifference <= -15 -> -2
            else -> 0
        }

        return morale.copy(
            morale = (
                morale.morale + moraleChange
                ).coerceIn(0, 100),
            happiness = (
                morale.happiness + happinessChange
                ).coerceIn(0, 100),
            recentMinutesRate = minutesRate
        )
    }

    fun applyTeamTalk(
        morale: PlayerMorale,
        result: TeamTalkResult
    ): PlayerMorale =
        morale.copy(
            morale = (
                morale.morale +
                    result.moraleChange
                ).coerceIn(0, 100),
            managerRelationship = (
                morale.managerRelationship +
                    result.managerRelationshipChange
                ).coerceIn(0, 100)
        )

    fun trainingWeek(
        morale: PlayerMorale,
        trainingIntensity: Int,
        performance: Int
    ): PlayerMorale {
        require(trainingIntensity in 0..100)
        require(performance in 0..100)

        val moraleChange = when {
            performance >= 80 -> 2
            performance <= 35 -> -2
            else -> 0
        }

        val happinessChange = when {
            trainingIntensity >= 90 -> -2
            trainingIntensity <= 30 -> 1
            else -> 0
        }

        return morale.copy(
            morale = (
                morale.morale + moraleChange
                ).coerceIn(0, 100),
            happiness = (
                morale.happiness + happinessChange
                ).coerceIn(0, 100)
        )
    }

    fun performanceModifier(
        morale: PlayerMorale
    ): Double = when (morale.level) {
        MoraleLevel.VERY_HIGH -> 1.05
        MoraleLevel.HIGH -> 1.025
        MoraleLevel.NORMAL -> 1.0
        MoraleLevel.LOW -> 0.97
        MoraleLevel.VERY_LOW -> 0.93
    }
}
