package com.wikisoccerclub.data.finance

enum class SponsorshipStatus {
    OFFERED,
    ACTIVE,
    REJECTED,
    EXPIRED
}

data class SponsorshipOffer(
    val id: String,
    val clubId: String,
    val sponsorName: String,
    val startYear: Int,
    val durationYears: Int,
    val signingBonus: Long,
    val monthlyPayment: Long,
    val championshipBonus: Long = 0,
    val objectiveDescription: String = "",
    val status: SponsorshipStatus =
        SponsorshipStatus.OFFERED
)

data class ActiveSponsorship(
    val offerId: String,
    val clubId: String,
    val sponsorName: String,
    val startYear: Int,
    val endYear: Int,
    val monthlyPayment: Long,
    val championshipBonus: Long,
    val objectiveDescription: String
)
