package com.wikisoccerclub.data.youth

import kotlin.math.roundToInt
import kotlin.random.Random

object YouthAcademyEngine {

    fun validateAcademy(
        academy: YouthAcademy
    ) {
        require(academy.level in 1..10)
        require(academy.facilities in 0..100)
        require(academy.coachingQuality in 0..100)
        require(academy.recruitmentNetwork in 0..100)
        require(academy.monthlyCost >= 0)
        require(academy.capacity in 1..100)
    }

    fun trainMonth(
        academy: YouthAcademy,
        players: List<YouthPlayer>,
        random: Random = Random.Default
    ): Pair<List<YouthPlayer>, List<YouthTrainingResult>> {
        validateAcademy(academy)

        val activePlayers = players.filter {
            it.status == YouthStatus.ACADEMY ||
                it.status == YouthStatus.RESERVE_TEAM
        }

        require(activePlayers.size <= academy.capacity) {
            "A capacidade da academia foi excedida."
        }

        val results = mutableListOf<YouthTrainingResult>()

        val updated = players.map { player ->
            if (
                player.status != YouthStatus.ACADEMY &&
                player.status != YouthStatus.RESERVE_TEAM
            ) {
                player
            } else {
                val result = trainPlayer(
                    academy = academy,
                    player = player,
                    random = random
                )

                results += result

                player.copy(
                    overall =
                        (player.overall +
                            result.overallChange)
                            .coerceIn(0, 100),
                    physical =
                        (player.physical +
                            result.physicalChange)
                            .coerceIn(0, 100),
                    technical =
                        (player.technical +
                            result.technicalChange)
                            .coerceIn(0, 100),
                    tactical =
                        (player.tactical +
                            result.tacticalChange)
                            .coerceIn(0, 100),
                    morale =
                        (player.morale +
                            result.moraleChange)
                            .coerceIn(0, 100),
                    developmentPoints =
                        player.developmentPoints +
                            result.developmentPointsGained,
                    monthsInAcademy =
                        player.monthsInAcademy + 1
                )
            }
        }

        return updated to results
    }

    fun canPromote(
        player: YouthPlayer,
        minimumOverall: Int = 55
    ): Boolean =
        player.status == YouthStatus.ACADEMY &&
            player.age >= 16 &&
            player.overall >= minimumOverall

    fun promote(
        player: YouthPlayer
    ): YouthPlayer {
        require(canPromote(player)) {
            "O jogador ainda não pode ser promovido."
        }

        return player.copy(
            status = YouthStatus.PROMOTED,
            morale =
                (player.morale + 8)
                    .coerceIn(0, 100)
        )
    }

    fun release(
        player: YouthPlayer
    ): YouthPlayer =
        player.copy(
            status = YouthStatus.RELEASED,
            morale =
                (player.morale - 15)
                    .coerceIn(0, 100)
        )

    fun buildSummary(
        academy: YouthAcademy,
        players: List<YouthPlayer>
    ): YouthAcademySummary {
        val active = players.filter {
            it.status == YouthStatus.ACADEMY ||
                it.status == YouthStatus.RESERVE_TEAM
        }

        return YouthAcademySummary(
            totalPlayers = active.size,
            averageAge =
                active.map { it.age }
                    .average()
                    .takeUnless { it.isNaN() }
                    ?: 0.0,
            averageOverall =
                active.map { it.overall }
                    .average()
                    .takeUnless { it.isNaN() }
                    ?: 0.0,
            averagePotential =
                active.map { it.potential }
                    .average()
                    .takeUnless { it.isNaN() }
                    ?: 0.0,
            highestPotentialPlayerId =
                active.maxByOrNull {
                    it.potential
                }?.id,
            monthlyCost =
                academy.monthlyCost
        )
    }

    private fun trainPlayer(
        academy: YouthAcademy,
        player: YouthPlayer,
        random: Random
    ): YouthTrainingResult {
        val ageFactor = when {
            player.age <= 16 -> 1.20
            player.age <= 18 -> 1.00
            player.age <= 20 -> 0.80
            else -> 0.60
        }

        val potentialRoom =
            (player.potential - player.overall)
                .coerceAtLeast(0)

        val qualityFactor =
            (
                academy.facilities * 0.35 +
                    academy.coachingQuality * 0.45 +
                    academy.level * 2.0
                ) / 100.0

        val randomFactor =
            random.nextDouble(0.75, 1.25)

        val points = (
            (2.0 +
                potentialRoom / 12.0) *
                ageFactor *
                qualityFactor *
                randomFactor
            ).roundToInt()
            .coerceAtLeast(1)

        var physicalGain = 0
        var technicalGain = 0
        var tacticalGain = 0

        when (academy.focus) {
            AcademyFocus.BALANCED -> {
                physicalGain = points / 3
                technicalGain = points / 3
                tacticalGain =
                    points - physicalGain - technicalGain
            }
            AcademyFocus.TECHNICAL ->
                technicalGain = points
            AcademyFocus.PHYSICAL ->
                physicalGain = points
            AcademyFocus.TACTICAL ->
                tacticalGain = points
            AcademyFocus.DEFENSIVE -> {
                tacticalGain =
                    (points * 0.65).roundToInt()
                physicalGain =
                    points - tacticalGain
            }
            AcademyFocus.ATTACKING -> {
                technicalGain =
                    (points * 0.70).roundToInt()
                tacticalGain =
                    points - technicalGain
            }
            AcademyFocus.GOALKEEPING -> {
                if (
                    player.position ==
                    YouthPosition.GOALKEEPER
                ) {
                    technicalGain =
                        (points * 0.60).roundToInt()
                    tacticalGain =
                        points - technicalGain
                } else {
                    tacticalGain = points / 2
                    technicalGain =
                        points - tacticalGain
                }
            }
        }

        val attributeAverage =
            (
                player.physical +
                    player.technical +
                    player.tactical
                ) / 3

        val projectedAverage =
            (
                player.physical + physicalGain +
                    player.technical + technicalGain +
                    player.tactical + tacticalGain
                ) / 3

        val overallChange =
            if (
                projectedAverage >
                attributeAverage &&
                player.overall <
                player.potential
            ) {
                1
            } else {
                0
            }

        val moraleChange = when {
            points >= 8 -> 2
            points >= 4 -> 1
            else -> 0
        }

        return YouthTrainingResult(
            playerId = player.id,
            overallChange = overallChange,
            physicalChange = physicalGain,
            technicalChange = technicalGain,
            tacticalChange = tacticalGain,
            moraleChange = moraleChange,
            developmentPointsGained = points
        )
    }
}
