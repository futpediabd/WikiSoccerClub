package com.wikisoccerclub.data.supporters

import kotlin.random.Random

object MembershipEngine {

    fun runCampaign(
        profile: SupporterProfile,
        campaign: MembershipCampaign,
        monthlyFee: Long,
        random: Random = Random.Default
    ): Pair<SupporterProfile, MembershipCampaignResult> {
        require(campaign.investment >= 0)
        require(campaign.durationDays in 1..365)
        require(campaign.benefitQuality in 0..100)
        require(monthlyFee >= 0)

        val reach = (
            profile.totalFans *
                (
                    0.01 +
                        campaign.investment /
                            100_000_000.0 +
                        campaign.benefitQuality /
                            500.0
                    )
            ).toLong()

        val conversionRate =
            (
                0.01 +
                    profile.loyalty / 2_000.0 +
                    profile.satisfaction / 3_000.0 +
                    campaign.benefitQuality / 1_500.0
                ).coerceIn(0.01, 0.18)

        val variation =
            random.nextDouble(0.85, 1.15)

        val newMembers = (
            reach * conversionRate * variation
            ).toInt().coerceAtLeast(0)

        val grossRevenue =
            newMembers * monthlyFee * 12

        val netRevenue =
            grossRevenue - campaign.investment

        val satisfactionChange = when {
            campaign.benefitQuality >= 85 -> 4
            campaign.benefitQuality >= 65 -> 2
            campaign.benefitQuality < 35 -> -2
            else -> 0
        }

        val result = MembershipCampaignResult(
            campaignId = campaign.id,
            newMembers = newMembers,
            grossRevenue = grossRevenue,
            netRevenue = netRevenue,
            satisfactionChange = satisfactionChange
        )

        val updated = profile.copy(
            activeMembers =
                profile.activeMembers + newMembers,
            satisfaction =
                (profile.satisfaction +
                    satisfactionChange)
                    .coerceIn(0, 100),
            engagement =
                (profile.engagement +
                    if (newMembers > 0) 2 else 0)
                    .coerceIn(0, 100)
        )

        return updated to result
    }
}
