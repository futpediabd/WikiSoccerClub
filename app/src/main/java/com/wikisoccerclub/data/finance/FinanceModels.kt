package com.wikisoccerclub.data.finance

enum class FinanceTransactionType {
    INITIAL_BALANCE,
    TICKET_REVENUE,
    SPONSORSHIP,
    PRIZE_MONEY,
    PLAYER_SALE,
    PLAYER_PURCHASE,
    PLAYER_WAGES,
    STAFF_WAGES,
    YOUTH_ACADEMY,
    SCOUTING,
    STADIUM_MAINTENANCE,
    OTHER_INCOME,
    OTHER_EXPENSE
}

data class FinanceTransaction(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val month: Int,
    val type: FinanceTransactionType,
    val description: String,
    val amount: Long
) {
    val isIncome: Boolean
        get() = amount > 0

    val isExpense: Boolean
        get() = amount < 0
}

data class ClubFinance(
    val clubId: String,
    val balance: Long,
    val monthlyPlayerWages: Long = 0,
    val monthlyStaffWages: Long = 0,
    val monthlyAcademyCost: Long = 0,
    val monthlyStadiumCost: Long = 0,
    val wageBudget: Long = 0,
    val transferBudget: Long = 0
)

data class MonthlyFinanceSummary(
    val clubId: String,
    val seasonYear: Int,
    val month: Int,
    val income: Long,
    val expenses: Long,
    val result: Long,
    val closingBalance: Long
)
