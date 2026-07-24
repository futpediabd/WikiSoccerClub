package com.wikisoccerclub.data.stadium

enum class StadiumFacility {
    SEATING,
    PITCH,
    LIGHTING,
    DRAINAGE,
    SECURITY,
    HOSPITALITY,
    PARKING
}

enum class TicketCategory {
    POPULAR,
    STANDARD,
    PREMIUM,
    VIP
}

data class Stadium(
    val id: String,
    val clubId: String,
    val name: String,
    val city: String,
    val capacity: Int,
    val pitchQuality: Int = 50,
    val lightingQuality: Int = 50,
    val drainageQuality: Int = 50,
    val securityQuality: Int = 50,
    val hospitalityQuality: Int = 50,
    val parkingQuality: Int = 50,
    val maintenanceCostPerMonth: Long = 0,
    val isOwnedByClub: Boolean = true
)

data class TicketPrices(
    val popular: Long,
    val standard: Long,
    val premium: Long,
    val vip: Long
) {
    fun priceFor(category: TicketCategory): Long =
        when (category) {
            TicketCategory.POPULAR -> popular
            TicketCategory.STANDARD -> standard
            TicketCategory.PREMIUM -> premium
            TicketCategory.VIP -> vip
        }
}

data class MatchAttendanceInput(
    val matchId: String,
    val stadium: Stadium,
    val homeClubReputation: Int,
    val awayClubReputation: Int,
    val competitionReputation: Int,
    val rivalryIntensity: Int,
    val homeForm: Int,
    val weatherPenalty: Int,
    val ticketPrices: TicketPrices,
    val averageLocalIncomeIndex: Int = 50,
    val isFinal: Boolean = false,
    val isTitleDecider: Boolean = false
)

data class MatchAttendanceResult(
    val matchId: String,
    val attendance: Int,
    val occupancyRate: Double,
    val popularTickets: Int,
    val standardTickets: Int,
    val premiumTickets: Int,
    val vipTickets: Int,
    val grossRevenue: Long,
    val operatingCost: Long,
    val netRevenue: Long
)

data class StadiumUpgrade(
    val id: String,
    val stadiumId: String,
    val facility: StadiumFacility,
    val currentLevel: Int,
    val targetLevel: Int,
    val cost: Long,
    val durationDays: Int,
    val startedDay: Int,
    val completed: Boolean = false
)
