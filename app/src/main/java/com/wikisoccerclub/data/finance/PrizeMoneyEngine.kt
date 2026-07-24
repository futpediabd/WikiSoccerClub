package com.wikisoccerclub.data.finance

data class CompetitionPrizeRule(
    val competitionId: String,
    val championPrize: Long,
    val runnerUpPrize: Long = 0,
    val semifinalPrize: Long = 0,
    val participationPrize: Long = 0
)

enum class CompetitionFinish {
    CHAMPION,
    RUNNER_UP,
    SEMIFINAL,
    PARTICIPATION
}

object PrizeMoneyEngine {

    fun createPrizeTransaction(
        clubId: String,
        competitionName: String,
        rule: CompetitionPrizeRule,
        finish: CompetitionFinish,
        seasonYear: Int,
        month: Int
    ): FinanceTransaction {
        val value = when (finish) {
            CompetitionFinish.CHAMPION ->
                rule.championPrize
            CompetitionFinish.RUNNER_UP ->
                rule.runnerUpPrize
            CompetitionFinish.SEMIFINAL ->
                rule.semifinalPrize
            CompetitionFinish.PARTICIPATION ->
                rule.participationPrize
        }

        require(value >= 0) {
            "O prêmio não pode ser negativo."
        }

        return FinanceTransaction(
            id = "premio_${rule.competitionId}_${clubId}_${seasonYear}_${finish.name}",
            clubId = clubId,
            seasonYear = seasonYear,
            month = month,
            type = FinanceTransactionType.PRIZE_MONEY,
            description = "Premiação $competitionName: $finish",
            amount = value
        )
    }
}
