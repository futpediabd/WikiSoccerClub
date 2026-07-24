package com.wikisoccerclub.data.finance

object FinanceEngine {

    fun validateFinance(finance: ClubFinance):
        Result<Unit> = runCatching {
        require(finance.clubId.isNotBlank()) {
            "O clube precisa ser informado."
        }
        require(finance.monthlyPlayerWages >= 0) {
            "A folha salarial dos jogadores não pode ser negativa."
        }
        require(finance.monthlyStaffWages >= 0) {
            "A folha salarial da comissão não pode ser negativa."
        }
        require(finance.monthlyAcademyCost >= 0) {
            "O custo da base não pode ser negativo."
        }
        require(finance.monthlyStadiumCost >= 0) {
            "O custo do estádio não pode ser negativo."
        }
        require(finance.wageBudget >= 0) {
            "O orçamento salarial não pode ser negativo."
        }
        require(finance.transferBudget >= 0) {
            "O orçamento de transferências não pode ser negativo."
        }
    }

    fun applyTransaction(
        finance: ClubFinance,
        transaction: FinanceTransaction
    ): ClubFinance {
        require(finance.clubId == transaction.clubId) {
            "A transação pertence a outro clube."
        }

        return finance.copy(
            balance = finance.balance + transaction.amount
        )
    }

    fun createMonthlyExpenses(
        finance: ClubFinance,
        seasonYear: Int,
        month: Int
    ): List<FinanceTransaction> {
        require(month in 1..12) {
            "O mês precisa ficar entre 1 e 12."
        }

        return buildList {
            if (finance.monthlyPlayerWages > 0) {
                add(
                    FinanceTransaction(
                        id = "sal_jog_${finance.clubId}_${seasonYear}_$month",
                        clubId = finance.clubId,
                        seasonYear = seasonYear,
                        month = month,
                        type = FinanceTransactionType.PLAYER_WAGES,
                        description = "Salários dos jogadores",
                        amount = -finance.monthlyPlayerWages
                    )
                )
            }

            if (finance.monthlyStaffWages > 0) {
                add(
                    FinanceTransaction(
                        id = "sal_com_${finance.clubId}_${seasonYear}_$month",
                        clubId = finance.clubId,
                        seasonYear = seasonYear,
                        month = month,
                        type = FinanceTransactionType.STAFF_WAGES,
                        description = "Salários da comissão técnica",
                        amount = -finance.monthlyStaffWages
                    )
                )
            }

            if (finance.monthlyAcademyCost > 0) {
                add(
                    FinanceTransaction(
                        id = "base_${finance.clubId}_${seasonYear}_$month",
                        clubId = finance.clubId,
                        seasonYear = seasonYear,
                        month = month,
                        type = FinanceTransactionType.YOUTH_ACADEMY,
                        description = "Manutenção das categorias de base",
                        amount = -finance.monthlyAcademyCost
                    )
                )
            }

            if (finance.monthlyStadiumCost > 0) {
                add(
                    FinanceTransaction(
                        id = "est_${finance.clubId}_${seasonYear}_$month",
                        clubId = finance.clubId,
                        seasonYear = seasonYear,
                        month = month,
                        type = FinanceTransactionType.STADIUM_MAINTENANCE,
                        description = "Manutenção do estádio",
                        amount = -finance.monthlyStadiumCost
                    )
                )
            }
        }
    }

    fun processMonth(
        finance: ClubFinance,
        seasonYear: Int,
        month: Int,
        extraTransactions: List<FinanceTransaction> =
            emptyList()
    ): Pair<ClubFinance, MonthlyFinanceSummary> {
        validateFinance(finance).getOrThrow()

        val transactions =
            createMonthlyExpenses(
                finance = finance,
                seasonYear = seasonYear,
                month = month
            ) + extraTransactions.filter {
                it.clubId == finance.clubId &&
                    it.seasonYear == seasonYear &&
                    it.month == month
            }

        val updated = transactions.fold(finance) {
                current,
                transaction
            ->
            applyTransaction(current, transaction)
        }

        val income = transactions
            .filter { it.amount > 0 }
            .sumOf { it.amount }

        val expenses = transactions
            .filter { it.amount < 0 }
            .sumOf { -it.amount }

        return updated to MonthlyFinanceSummary(
            clubId = finance.clubId,
            seasonYear = seasonYear,
            month = month,
            income = income,
            expenses = expenses,
            result = income - expenses,
            closingBalance = updated.balance
        )
    }

    fun availableWageBudget(
        finance: ClubFinance
    ): Long =
        (finance.wageBudget -
            finance.monthlyPlayerWages)
            .coerceAtLeast(0)

    fun canAffordTransfer(
        finance: ClubFinance,
        transferValue: Long
    ): Boolean =
        transferValue >= 0 &&
            transferValue <= finance.transferBudget &&
            transferValue <= finance.balance
}
