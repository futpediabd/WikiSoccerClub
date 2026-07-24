package com.wikisoccerclub.data.youth

import kotlin.math.roundToInt
import kotlin.random.Random

object YouthDevelopmentEngine {

    fun project(
        player: YouthPlayer,
        academy: YouthAcademy
    ): YouthDevelopmentProjection {
        val developmentRoom =
            (player.potential -
                player.overall)
                .coerceAtLeast(0)

        val academyQuality =
            (
                academy.facilities * 0.35 +
                    academy.coachingQuality * 0.45 +
                    academy.level * 2.0
                ) / 100.0

        val yearsTo18 =
            (18 - player.age)
                .coerceAtLeast(0)

        val yearsTo21 =
            (21 - player.age)
                .coerceAtLeast(0)

        val projectedAt18 = (
            player.overall +
                developmentRoom *
                    academyQuality *
                    (yearsTo18 / 6.0)
            ).roundToInt()
            .coerceAtMost(player.potential)

        val projectedAt21 = (
            player.overall +
                developmentRoom *
                    academyQuality *
                    (yearsTo21 / 7.0)
            ).roundToInt()
            .coerceAtMost(player.potential)

        val risk = (
            100 -
                academy.coachingQuality / 2 -
                academy.facilities / 3 +
                when {
                    player.morale < 40 -> 25
                    player.morale < 60 -> 12
                    else -> 0
                } +
                when {
                    player.form < 40 -> 15
                    player.form < 60 -> 6
                    else -> 0
                }
            ).coerceIn(0, 100)

        val label = when {
            projectedAt21 >= 85 ->
                "Talento de elite"
            projectedAt21 >= 75 ->
                "Grande promessa"
            projectedAt21 >= 65 ->
                "Bom potencial profissional"
            projectedAt21 >= 55 ->
                "Projeto de jogador útil"
            else ->
                "Desenvolvimento incerto"
        }

        return YouthDevelopmentProjection(
            playerId = player.id,
            projectedOverallAt18 =
                projectedAt18,
            projectedOverallAt21 =
                projectedAt21,
            developmentRisk = risk,
            developmentLabel = label
        )
    }

    fun applyQuarterlyReview(
        player: YouthPlayer,
        academy: YouthAcademy,
        random: Random = Random.Default
    ): YouthPlayer {
        if (
            player.status != YouthStatus.ACADEMY &&
            player.status != YouthStatus.RESERVE_TEAM
        ) {
            return player
        }

        val projection =
            project(player, academy)

        val growthChance = (
            academy.coachingQuality / 2 +
                academy.facilities / 3 +
                (player.potential -
                    player.overall) / 2 +
                player.morale / 5
            ).coerceIn(5, 95)

        val grows =
            random.nextInt(100) < growthChance

        val growth =
            if (grows &&
                player.overall < player.potential
            ) {
                when {
                    projection.developmentRisk < 20 -> 2
                    projection.developmentRisk < 50 -> 1
                    else -> 0
                }
            } else {
                0
            }

        val moraleChange = when {
            growth >= 2 -> 4
            growth == 1 -> 2
            else -> -1
        }

        return player.copy(
            overall =
                (player.overall + growth)
                    .coerceAtMost(
                        player.potential
                    ),
            morale =
                (player.morale +
                    moraleChange)
                    .coerceIn(0, 100),
            form =
                (player.form +
                    random.nextInt(-4, 5))
                    .coerceIn(0, 100)
        )
    }

    fun applyBirthday(
        player: YouthPlayer
    ): YouthPlayer =
        player.copy(
            age = player.age + 1
        )
}
