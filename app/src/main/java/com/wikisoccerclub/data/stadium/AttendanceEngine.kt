package com.wikisoccerclub.data.stadium

import kotlin.math.roundToInt
import kotlin.random.Random

object AttendanceEngine {

    fun calculate(
        input: MatchAttendanceInput,
        random: Random = Random.Default
    ): MatchAttendanceResult {
        validate(input)

        val reputationDemand = (
            input.homeClubReputation * 0.38 +
                input.awayClubReputation * 0.18 +
                input.competitionReputation * 0.24
            )

        val eventBonus =
            input.rivalryIntensity * 0.12 +
                if (input.isFinal) 15.0 else 0.0 +
                if (input.isTitleDecider) 12.0 else 0.0

        val formEffect =
            (input.homeForm - 50) * 0.18

        val priceEffect =
            priceDemandModifier(
                prices = input.ticketPrices,
                incomeIndex =
                    input.averageLocalIncomeIndex
            )

        val weatherEffect =
            -input.weatherPenalty.coerceIn(0, 40)
                .toDouble()

        val randomVariation =
            random.nextDouble(-5.0, 5.0)

        val demandPercent = (
            reputationDemand +
                eventBonus +
                formEffect +
                priceEffect +
                weatherEffect +
                randomVariation
            ).coerceIn(5.0, 100.0)

        val attendance = (
            input.stadium.capacity *
                demandPercent / 100.0
            ).roundToInt()
            .coerceIn(0, input.stadium.capacity)

        val distribution =
            distributeTickets(attendance)

        val grossRevenue =
            distribution.popular *
                input.ticketPrices.popular +
                distribution.standard *
                    input.ticketPrices.standard +
                distribution.premium *
                    input.ticketPrices.premium +
                distribution.vip *
                    input.ticketPrices.vip

        val operatingCost = (
            grossRevenue * 0.12
            ).toLong() +
            input.stadium.maintenanceCostPerMonth / 4

        return MatchAttendanceResult(
            matchId = input.matchId,
            attendance = attendance,
            occupancyRate =
                if (input.stadium.capacity == 0) 0.0
                else attendance.toDouble() /
                    input.stadium.capacity,
            popularTickets = distribution.popular,
            standardTickets = distribution.standard,
            premiumTickets = distribution.premium,
            vipTickets = distribution.vip,
            grossRevenue = grossRevenue,
            operatingCost = operatingCost,
            netRevenue =
                (grossRevenue - operatingCost)
                    .coerceAtLeast(0)
        )
    }

    private data class Distribution(
        val popular: Int,
        val standard: Int,
        val premium: Int,
        val vip: Int
    )

    private fun distributeTickets(
        attendance: Int
    ): Distribution {
        val vip = (attendance * 0.03).roundToInt()
        val premium =
            (attendance * 0.12).roundToInt()
        val standard =
            (attendance * 0.50).roundToInt()
        val popular =
            attendance - vip - premium - standard

        return Distribution(
            popular = popular,
            standard = standard,
            premium = premium,
            vip = vip
        )
    }

    private fun priceDemandModifier(
        prices: TicketPrices,
        incomeIndex: Int
    ): Double {
        val average = (
            prices.popular +
                prices.standard +
                prices.premium +
                prices.vip
            ) / 4.0

        val affordableReference =
            20.0 + incomeIndex.coerceIn(0, 100) * 1.8

        return when {
            average <= affordableReference * 0.7 -> 10.0
            average <= affordableReference -> 4.0
            average <= affordableReference * 1.3 -> -4.0
            average <= affordableReference * 1.8 -> -12.0
            else -> -24.0
        }
    }

    private fun validate(
        input: MatchAttendanceInput
    ) {
        require(input.stadium.capacity > 0)
        require(input.homeClubReputation in 0..100)
        require(input.awayClubReputation in 0..100)
        require(input.competitionReputation in 0..100)
        require(input.rivalryIntensity in 0..100)
        require(input.homeForm in 0..100)
        require(input.weatherPenalty in 0..100)
        require(input.averageLocalIncomeIndex in 0..100)
        require(
            listOf(
                input.ticketPrices.popular,
                input.ticketPrices.standard,
                input.ticketPrices.premium,
                input.ticketPrices.vip
            ).all { it >= 0 }
        )
    }
}
