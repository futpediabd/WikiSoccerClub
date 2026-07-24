package com.wikisoccerclub.data.commercial

enum class SponsorCategory {
    MAIN_SHIRT,
    SLEEVE,
    KIT_SUPPLIER,
    STADIUM_NAMING,
    TRAINING_CENTER,
    REGIONAL_PARTNER
}

enum class SponsorObjectiveType {
    LEAGUE_POSITION,
    WIN_COMPETITION,
    REACH_KNOCKOUT_STAGE,
    SIGN_REPUTABLE_PLAYER,
    USE_YOUTH_PLAYERS,
    AVERAGE_ATTENDANCE,
    SOCIAL_GROWTH
}

enum class ContractStatus {
    OFFERED,
    ACTIVE,
    COMPLETED,
    TERMINATED,
    EXPIRED
}

data class SponsorObjective(
    val type: SponsorObjectiveType,
    val targetValue: Int,
    val currentValue: Int = 0,
    val bonusValue: Long = 0,
    val penaltyValue: Long = 0,
    val completed: Boolean = false,
    val failed: Boolean = false
)

data class SponsorOffer(
    val id: String,
    val sponsorName: String,
    val category: SponsorCategory,
    val durationSeasons: Int,
    val fixedAnnualValue: Long,
    val signingBonus: Long,
    val reputationRequirement: Int,
    val exclusivityRequired: Boolean,
    val objectives: List<SponsorObjective> = emptyList()
)

data class SponsorContract(
    val id: String,
    val clubId: String,
    val sponsorName: String,
    val category: SponsorCategory,
    val startSeason: Int,
    val endSeason: Int,
    val fixedAnnualValue: Long,
    val signingBonus: Long,
    val objectives: List<SponsorObjective>,
    val status: ContractStatus = ContractStatus.ACTIVE,
    val totalReceived: Long = 0
)

data class MerchandiseProfile(
    val clubId: String,
    val fanBase: Int,
    val clubReputation: Int,
    val starPlayers: Int,
    val recentTitles: Int,
    val internationalReach: Int,
    val shirtPrice: Long,
    val productQuality: Int
)

data class MerchandiseResult(
    val seasonYear: Int,
    val unitsSold: Int,
    val grossRevenue: Long,
    val productionCost: Long,
    val netRevenue: Long,
    val popularityChange: Int
)

data class CommercialSummary(
    val activeContracts: List<SponsorContract>,
    val annualSponsorIncome: Long,
    val objectiveBonuses: Long,
    val merchandiseRevenue: Long,
    val totalCommercialRevenue: Long
)
