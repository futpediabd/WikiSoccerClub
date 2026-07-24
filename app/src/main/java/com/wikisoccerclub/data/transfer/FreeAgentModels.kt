package com.wikisoccerclub.data.transfer

data class FreeAgent(
    val playerId: String,
    val previousClubId: String? = null,
    val minimumSalary: Long,
    val preferredContractYears: Int = 2
)

data class FreeAgentSigningRequest(
    val id: String,
    val freeAgent: FreeAgent,
    val clubId: String,
    val seasonYear: Int,
    val monthlySalary: Long,
    val contractYears: Int,
    val signingBonus: Long = 0
)
