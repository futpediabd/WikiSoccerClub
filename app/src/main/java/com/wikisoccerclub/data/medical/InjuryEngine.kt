package com.wikisoccerclub.data.medical

import kotlin.math.roundToInt
import kotlin.random.Random

object InjuryEngine {

    fun injuryProbability(
        profile: PlayerMedicalProfile,
        minutesPlayed: Int,
        matchIntensity: Int
    ): Double {
        require(minutesPlayed in 0..130) {
            "Os minutos jogados são inválidos."
        }
        require(matchIntensity in 0..100) {
            "A intensidade deve ficar entre 0 e 100."
        }

        val base = 0.004
        val pronenessFactor =
            profile.injuryProneness.coerceIn(0, 100) /
                1800.0
        val fatigueFactor =
            profile.fatigue.coerceIn(0, 100) / 1400.0
        val conditionPenalty =
            (100 - profile.physicalCondition
                .coerceIn(0, 100)) / 1800.0
        val minutesFactor =
            minutesPlayed / 90.0
        val intensityFactor =
            0.65 + matchIntensity / 150.0

        return (
            base +
                pronenessFactor +
                fatigueFactor +
                conditionPenalty
            ) * minutesFactor * intensityFactor
    }

    fun rollInjury(
        profile: PlayerMedicalProfile,
        minutesPlayed: Int,
        matchIntensity: Int,
        seasonYear: Int,
        currentDay: Int,
        random: Random = Random.Default
    ): Injury? {
        val probability = injuryProbability(
            profile = profile,
            minutesPlayed = minutesPlayed,
            matchIntensity = matchIntensity
        )

        if (random.nextDouble() > probability) {
            return null
        }

        val severity = rollSeverity(
            profile = profile,
            random = random
        )
        val duration = recoveryDays(
            severity = severity,
            recoveryRate = profile.recoveryRate,
            random = random
        )

        return Injury(
            id = "lesao_${profile.playerId}_" +
                "${seasonYear}_$currentDay",
            playerId = profile.playerId,
            description = descriptionFor(
                severity = severity,
                random = random
            ),
            severity = severity,
            startSeasonYear = seasonYear,
            startDay = currentDay,
            expectedRecoveryDays = duration
        )
    }

    fun advanceRecovery(
        injury: Injury,
        days: Int,
        medicalDepartmentLevel: Int
    ): Injury {
        require(days >= 0) {
            "A quantidade de dias não pode ser negativa."
        }
        require(medicalDepartmentLevel in 1..10) {
            "O nível médico deve ficar entre 1 e 10."
        }

        if (injury.status == InjuryStatus.CLEARED) {
            return injury
        }

        val bonus = when {
            medicalDepartmentLevel >= 9 -> 0.30
            medicalDepartmentLevel >= 7 -> 0.20
            medicalDepartmentLevel >= 4 -> 0.10
            else -> 0.0
        }

        val effectiveDays =
            (days * (1.0 + bonus))
                .roundToInt()
                .coerceAtLeast(days)

        val elapsed = (
            injury.elapsedRecoveryDays + effectiveDays
            ).coerceAtMost(injury.expectedRecoveryDays)

        return injury.copy(
            elapsedRecoveryDays = elapsed,
            status = when {
                elapsed >= injury.expectedRecoveryDays ->
                    InjuryStatus.CLEARED
                elapsed >= injury.expectedRecoveryDays / 2 ->
                    InjuryStatus.RECOVERING
                else -> InjuryStatus.ACTIVE
            }
        )
    }

    fun canPlay(injury: Injury?): Boolean =
        injury == null ||
            injury.status == InjuryStatus.CLEARED

    private fun rollSeverity(
        profile: PlayerMedicalProfile,
        random: Random
    ): InjurySeverity {
        val risk = random.nextInt(100) +
            profile.injuryProneness / 5 +
            profile.fatigue / 10

        return when {
            risk >= 108 -> InjurySeverity.SEVERE
            risk >= 85 -> InjurySeverity.SERIOUS
            risk >= 55 -> InjurySeverity.MODERATE
            else -> InjurySeverity.MINOR
        }
    }

    private fun recoveryDays(
        severity: InjurySeverity,
        recoveryRate: Int,
        random: Random
    ): Int {
        val base = when (severity) {
            InjurySeverity.MINOR ->
                random.nextInt(3, 9)
            InjurySeverity.MODERATE ->
                random.nextInt(10, 29)
            InjurySeverity.SERIOUS ->
                random.nextInt(30, 91)
            InjurySeverity.SEVERE ->
                random.nextInt(90, 241)
        }

        val reduction =
            recoveryRate.coerceIn(0, 100) / 250.0

        return (base * (1.0 - reduction))
            .roundToInt()
            .coerceAtLeast(2)
    }

    private fun descriptionFor(
        severity: InjurySeverity,
        random: Random
    ): String {
        val options = when (severity) {
            InjurySeverity.MINOR -> listOf(
                "Contusão leve",
                "Desconforto muscular",
                "Torção leve"
            )
            InjurySeverity.MODERATE -> listOf(
                "Distensão muscular",
                "Entorse",
                "Lesão no tornozelo"
            )
            InjurySeverity.SERIOUS -> listOf(
                "Lesão muscular grave",
                "Fratura",
                "Lesão ligamentar"
            )
            InjurySeverity.SEVERE -> listOf(
                "Ruptura de ligamento",
                "Fratura grave",
                "Lesão no tendão"
            )
        }

        return options.random(random)
    }
}
