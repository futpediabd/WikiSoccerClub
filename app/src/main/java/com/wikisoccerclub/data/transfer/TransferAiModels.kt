package com.wikisoccerclub.data.transfer

enum class SquadNeedPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

data class SquadNeed(
    val clubId: String,
    val position: String,
    val priority: SquadNeedPriority,
    val minimumOverall: Int,
    val maximumAge: Int? = null
)

data class MarketPlayer(
    val playerId: String,
    val clubId: String?,
    val position: String,
    val age: Int,
    val overall: Int,
    val potential: Int,
    val marketValue: Long,
    val monthlySalary: Long,
    val listedForSale: Boolean = false,
    val availableForLoan: Boolean = false
)

data class AiClubProfile(
    val clubId: String,
    val balance: Long,
    val reputation: Int,
    val squadPlayerIds: List<String>,
    val needs: List<SquadNeed>
)

data class AiTransferDecision(
    val clubId: String,
    val playerId: String,
    val transferValue: Long,
    val proposedSalary: Long,
    val score: Int,
    val reason: String
)
