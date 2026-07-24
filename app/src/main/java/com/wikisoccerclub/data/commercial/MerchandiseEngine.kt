package com.wikisoccerclub.data.commercial

import kotlin.math.roundToInt
import kotlin.random.Random

object MerchandiseEngine {

    fun calculateSeason(
        profile: MerchandiseProfile,
        seasonYear: Int,
        random: Random = Random.Default
    ): MerchandiseResult {
        validate(profile)

        val demandScore =
            profile.fanBase * 0.32 +
                profile.clubReputation * 0.28 +
                profile.starPlayers * 7.0 +
                profile.recentTitles * 12.0 +
                profile.internationalReach * 0.18 +
                profile.productQuality * 0.10

        val pricePenalty =
            when {
                profile.shirtPrice <= 80 -> 1.15
                profile.shirtPrice <= 150 -> 1.00
                profile.shirtPrice <= 250 -> 0.82
                else -> 0.62
            }

        val randomFactor =
            random.nextDouble(0.90, 1.12)

        val units = (
            demandScore *
                1_000 *
                pricePenalty *
                randomFactor
            ).roundToInt()
            .coerceAtLeast(0)

        val gross =
            units * profile.shirtPrice

        val unitProductionCost = (
            18L +
                profile.productQuality * 55L / 100L
            )

        val production =
            units * unitProductionCost

        val popularityChange = when {
            profile.productQuality >= 85 -> 3
            profile.productQuality >= 65 -> 1
            profile.productQuality < 35 -> -2
            else -> 0
        }

        return MerchandiseResult(
            seasonYear = seasonYear,
            unitsSold = units,
            grossRevenue = gross,
            productionCost = production,
            netRevenue =
                (gross - production)
                    .coerceAtLeast(0),
            popularityChange =
                popularityChange
        )
    }

    private fun validate(
        profile: MerchandiseProfile
    ) {
        require(profile.fanBase in 0..100)
        require(profile.clubReputation in 0..100)
        require(profile.starPlayers >= 0)
        require(profile.recentTitles >= 0)
        require(profile.internationalReach in 0..100)
        require(profile.shirtPrice >= 0)
        require(profile.productQuality in 0..100)
    }
}
