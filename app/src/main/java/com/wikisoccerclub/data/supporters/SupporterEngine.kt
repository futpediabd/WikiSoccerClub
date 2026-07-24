package com.wikisoccerclub.data.supporters

import kotlin.math.roundToLong

object SupporterEngine {

    fun applyEvent(
        profile: SupporterProfile,
        event: SupporterEvent
    ): Pair<SupporterProfile, SupporterEventResult> {
        require(event.importance in 0..100)

        val base = baseEffect(event.type)
        val scale = 0.50 + event.importance / 100.0

        val satisfaction =
            (base.satisfaction * scale).toInt()
        val loyalty =
            (base.loyalty * scale).toInt()
        val engagement =
            (base.engagement * scale).toInt()

        val fanGrowth = (
            profile.totalFans *
                base.fanGrowthRate *
                scale
            ).roundToLong()

        val memberGrowth = (
            profile.activeMembers *
                base.memberGrowthRate *
                scale
            ).toInt()

        val result = SupporterEventResult(
            satisfactionChange = satisfaction,
            loyaltyChange = loyalty,
            engagementChange = engagement,
            fanGrowth = fanGrowth,
            membershipGrowth = memberGrowth,
            message = event.description
        )

        val updated = profile.copy(
            totalFans =
                (profile.totalFans + fanGrowth)
                    .coerceAtLeast(0),
            activeMembers =
                (profile.activeMembers + memberGrowth)
                    .coerceAtLeast(0),
            loyalty =
                (profile.loyalty + loyalty)
                    .coerceIn(0, 100),
            satisfaction =
                (profile.satisfaction + satisfaction)
                    .coerceIn(0, 100),
            engagement =
                (profile.engagement + engagement)
                    .coerceIn(0, 100)
        )

        return updated to result
    }

    fun updateAttendance(
        profile: SupporterProfile,
        attendance: Int,
        stadiumCapacity: Int
    ): SupporterProfile {
        require(attendance >= 0)
        require(stadiumCapacity > 0)

        val occupancy =
            attendance.toDouble() / stadiumCapacity

        val engagementChange = when {
            occupancy >= 0.90 -> 2
            occupancy >= 0.70 -> 1
            occupancy < 0.35 -> -2
            else -> 0
        }

        return profile.copy(
            averageAttendance =
                if (profile.averageAttendance == 0) {
                    attendance
                } else {
                    (
                        profile.averageAttendance * 3 +
                            attendance
                        ) / 4
                },
            engagement =
                (profile.engagement + engagementChange)
                    .coerceIn(0, 100)
        )
    }

    fun seasonTicketDemand(
        profile: SupporterProfile,
        stadiumCapacity: Int,
        averageTicketPrice: Long,
        clubReputation: Int
    ): Int {
        require(stadiumCapacity > 0)
        require(averageTicketPrice >= 0)
        require(clubReputation in 0..100)

        val priceModifier = when {
            averageTicketPrice <= 60 -> 1.15
            averageTicketPrice <= 120 -> 1.00
            averageTicketPrice <= 200 -> 0.82
            else -> 0.62
        }

        val demand = (
            stadiumCapacity *
                (0.10 +
                    profile.loyalty / 250.0 +
                    clubReputation / 500.0) *
                priceModifier
            ).toInt()

        return demand.coerceIn(0, stadiumCapacity)
    }

    private data class BaseEffect(
        val satisfaction: Int,
        val loyalty: Int,
        val engagement: Int,
        val fanGrowthRate: Double,
        val memberGrowthRate: Double
    )

    private fun baseEffect(
        type: SupporterEventType
    ): BaseEffect = when (type) {
        SupporterEventType.WIN ->
            BaseEffect(3, 1, 2, 0.001, 0.002)
        SupporterEventType.DRAW ->
            BaseEffect(0, 0, 0, 0.0, 0.0)
        SupporterEventType.DEFEAT ->
            BaseEffect(-3, 0, -1, -0.0003, 0.0)
        SupporterEventType.TITLE ->
            BaseEffect(18, 8, 12, 0.025, 0.040)
        SupporterEventType.RELEGATION ->
            BaseEffect(-25, -10, -12, -0.030, -0.100)
        SupporterEventType.PROMOTION ->
            BaseEffect(15, 7, 10, 0.020, 0.030)
        SupporterEventType.BIG_SIGNING ->
            BaseEffect(8, 2, 8, 0.008, 0.012)
        SupporterEventType.PLAYER_SALE ->
            BaseEffect(-6, -2, -3, -0.002, -0.005)
        SupporterEventType.TICKET_PRICE_CHANGE ->
            BaseEffect(-4, -1, -2, -0.001, -0.004)
        SupporterEventType.STADIUM_UPGRADE ->
            BaseEffect(7, 4, 5, 0.004, 0.008)
        SupporterEventType.RIVALRY_WIN ->
            BaseEffect(10, 4, 8, 0.006, 0.010)
        SupporterEventType.RIVALRY_DEFEAT ->
            BaseEffect(-10, -3, -6, -0.003, -0.010)
        SupporterEventType.BOARD_DECISION ->
            BaseEffect(-2, -1, -1, 0.0, -0.002)
    }
}
