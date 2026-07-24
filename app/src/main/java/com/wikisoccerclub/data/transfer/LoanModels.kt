package com.wikisoccerclub.data.transfer

enum class LoanStatus {
    PROPOSED,
    ACTIVE,
    REJECTED,
    COMPLETED,
    PURCHASED
}

data class LoanOffer(
    val id: String,
    val playerId: String,
    val ownerClubId: String,
    val destinationClubId: String,
    val startYear: Int,
    val endYear: Int,
    val monthlyFee: Long = 0,
    val wagePercentagePaidByDestination: Int = 100,
    val purchaseOption: Long? = null,
    val status: LoanStatus = LoanStatus.PROPOSED
)

data class ActiveLoan(
    val offerId: String,
    val playerId: String,
    val ownerClubId: String,
    val destinationClubId: String,
    val endYear: Int,
    val purchaseOption: Long? = null
)
