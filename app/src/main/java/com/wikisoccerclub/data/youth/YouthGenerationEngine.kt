package com.wikisoccerclub.data.youth

import kotlin.math.roundToInt
import kotlin.random.Random

object YouthGenerationEngine {

    fun generateIntake(
        academy: YouthAcademy,
        config: YouthIntakeConfig,
        namePools: List<YouthNamePool>,
        random: Random = Random.Default
    ): YouthIntakeResult {
        require(config.minimumPlayers in 1..30)
        require(config.maximumPlayers in config.minimumPlayers..40)
        require(config.minimumAge in 14..20)
        require(config.maximumAge in config.minimumAge..21)
        require(config.foreignPlayerChance in 0..100)
        require(config.goldenGenerationChance in 0..100)

        val amount =
            random.nextInt(
                config.minimumPlayers,
                config.maximumPlayers + 1
            )

        val goldenGeneration =
            random.nextInt(100) <
                config.goldenGenerationChance

        val generated = List(amount) { index ->
            generatePlayer(
                academy = academy,
                config = config,
                namePools = namePools,
                index = index,
                goldenGeneration = goldenGeneration,
                random = random
            )
        }

        return YouthIntakeResult(
            seasonYear = config.seasonYear,
            clubId = config.clubId,
            players = generated,
            goldenGeneration = goldenGeneration,
            averagePotential =
                generated.map { it.potential }
                    .average()
                    .takeUnless { it.isNaN() }
                    ?: 0.0,
            bestPlayerId =
                generated.maxByOrNull {
                    it.potential
                }?.id
        )
    }

    fun generatePlayer(
        academy: YouthAcademy,
        config: YouthIntakeConfig,
        namePools: List<YouthNamePool>,
        index: Int,
        goldenGeneration: Boolean,
        random: Random = Random.Default
    ): YouthPlayer {
        val isForeign =
            random.nextInt(100) <
                config.foreignPlayerChance

        val selectedPool =
            selectNamePool(
                country = config.country,
                foreign = isForeign,
                pools = namePools,
                random = random
            )

        val firstName =
            selectedPool?.firstNames
                ?.randomOrNull(random)
                ?: "Jogador"

        val lastName =
            selectedPool?.lastNames
                ?.randomOrNull(random)
                ?: "Jovem"

        val nationality =
            selectedPool?.nationality
                ?: config.country

        val age =
            random.nextInt(
                config.minimumAge,
                config.maximumAge + 1
            )

        val recruitmentBonus =
            academy.recruitmentNetwork / 8

        val facilityBonus =
            academy.facilities / 12

        val generationBonus =
            if (goldenGeneration) {
                random.nextInt(8, 18)
            } else {
                0
            }

        val potential = (
            random.nextInt(48, 82) +
                recruitmentBonus +
                generationBonus
            ).coerceIn(45, 100)

        val agePenalty =
            (age - 15).coerceAtLeast(0) * 2

        val overall = (
            random.nextInt(28, 49) +
                facilityBonus +
                academy.level +
                agePenalty
            ).coerceIn(25, potential - 1)

        val position =
            generatePosition(random)

        val attributes =
            generateAttributes(
                position = position,
                overall = overall,
                random = random
            )

        return YouthPlayer(
            id =
                buildString {
                    append("base_")
                    append(config.clubId)
                    append("_")
                    append(config.seasonYear)
                    append("_")
                    append(index)
                },
            name = "$firstName $lastName",
            nationality = nationality,
            age = age,
            position = position,
            overall = overall,
            potential = potential,
            physical = attributes.first,
            technical = attributes.second,
            tactical = attributes.third,
            morale = random.nextInt(58, 86),
            form = random.nextInt(45, 71),
            developmentPoints = 0,
            monthsInAcademy = 0,
            status = YouthStatus.ACADEMY
        )
    }

    private fun selectNamePool(
        country: String,
        foreign: Boolean,
        pools: List<YouthNamePool>,
        random: Random
    ): YouthNamePool? {
        if (pools.isEmpty()) {
            return null
        }

        val nationalPools =
            pools.filter {
                it.nationality.equals(
                    country,
                    ignoreCase = true
                )
            }

        return if (!foreign &&
            nationalPools.isNotEmpty()
        ) {
            nationalPools.random(random)
        } else {
            pools.filterNot {
                it.nationality.equals(
                    country,
                    ignoreCase = true
                )
            }.ifEmpty {
                pools
            }.random(random)
        }
    }

    private fun generatePosition(
        random: Random
    ): YouthPosition {
        val roll = random.nextInt(100)

        return when {
            roll < 10 ->
                YouthPosition.GOALKEEPER
            roll < 20 ->
                YouthPosition.RIGHT_BACK
            roll < 30 ->
                YouthPosition.LEFT_BACK
            roll < 46 ->
                YouthPosition.CENTER_BACK
            roll < 58 ->
                YouthPosition.DEFENSIVE_MIDFIELDER
            roll < 72 ->
                YouthPosition.CENTRAL_MIDFIELDER
            roll < 82 ->
                YouthPosition.ATTACKING_MIDFIELDER
            roll < 88 ->
                YouthPosition.RIGHT_WINGER
            roll < 94 ->
                YouthPosition.LEFT_WINGER
            else ->
                YouthPosition.STRIKER
        }
    }

    private fun generateAttributes(
        position: YouthPosition,
        overall: Int,
        random: Random
    ): Triple<Int, Int, Int> {
        var physical =
            overall + random.nextInt(-6, 7)
        var technical =
            overall + random.nextInt(-6, 7)
        var tactical =
            overall + random.nextInt(-6, 7)

        when (position) {
            YouthPosition.GOALKEEPER -> {
                technical += 4
                tactical += 3
            }
            YouthPosition.RIGHT_BACK,
            YouthPosition.LEFT_BACK -> {
                physical += 4
                tactical += 2
            }
            YouthPosition.CENTER_BACK -> {
                physical += 3
                tactical += 4
            }
            YouthPosition.DEFENSIVE_MIDFIELDER -> {
                tactical += 5
            }
            YouthPosition.CENTRAL_MIDFIELDER -> {
                technical += 3
                tactical += 3
            }
            YouthPosition.ATTACKING_MIDFIELDER -> {
                technical += 6
            }
            YouthPosition.RIGHT_WINGER,
            YouthPosition.LEFT_WINGER -> {
                physical += 3
                technical += 4
            }
            YouthPosition.STRIKER -> {
                technical += 4
                physical += 2
            }
        }

        return Triple(
            physical.coerceIn(1, 100),
            technical.coerceIn(1, 100),
            tactical.coerceIn(1, 100)
        )
    }
}
