package com.wikisoccerclub.data.transfer

enum class ContractNegotiationStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COUNTER
}

data class PlayerContract(
    val playerId: String,
    val clubId: String,
    val startYear: Int,
    val endYear: Int,
    val monthlySalary: Long,
    val releaseClause: Long? = null
)

data class ContractOffer(
    val id: String,
    val playerId: String,
    val clubId: String,
    val startYear: Int,
    val endYear: Int,
    val monthlySalary: Long,
    val signingBonus: Long = 0,
    val releaseClause: Long? = null,
    val status: ContractNegotiationStatus =
        ContractNegotiationStatus.PENDING
)
