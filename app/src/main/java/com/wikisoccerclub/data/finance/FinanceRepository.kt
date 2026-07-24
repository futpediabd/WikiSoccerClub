package com.wikisoccerclub.data.finance

class FinanceRepository {

    private val finances =
        linkedMapOf<String, ClubFinance>()

    private val transactions =
        linkedMapOf<String, FinanceTransaction>()

    private val summaries =
        mutableListOf<MonthlyFinanceSummary>()

    private val sponsorshipOffers =
        linkedMapOf<String, SponsorshipOffer>()

    private val activeSponsorships =
        linkedMapOf<String, ActiveSponsorship>()

    fun saveFinance(finance: ClubFinance) {
        finances[finance.clubId] = finance
    }

    fun findFinance(clubId: String):
        ClubFinance? = finances[clubId]

    fun saveTransaction(
        transaction: FinanceTransaction
    ) {
        transactions[transaction.id] = transaction
    }

    fun saveTransactions(
        values: List<FinanceTransaction>
    ) {
        values.forEach(::saveTransaction)
    }

    fun transactionsByClub(
        clubId: String
    ): List<FinanceTransaction> =
        transactions.values
            .filter { it.clubId == clubId }
            .sortedWith(
                compareByDescending<FinanceTransaction> {
                    it.seasonYear
                }.thenByDescending { it.month }
            )

    fun saveSummary(
        summary: MonthlyFinanceSummary
    ) {
        summaries.removeAll {
            it.clubId == summary.clubId &&
                it.seasonYear == summary.seasonYear &&
                it.month == summary.month
        }
        summaries += summary
    }

    fun summariesByClub(
        clubId: String
    ): List<MonthlyFinanceSummary> =
        summaries.filter {
            it.clubId == clubId
        }.sortedWith(
            compareByDescending<MonthlyFinanceSummary> {
                it.seasonYear
            }.thenByDescending { it.month }
        )

    fun allFinances(): List<ClubFinance> = finances.values.toList()

    fun allTransactions(): List<FinanceTransaction> = transactions.values.toList()

    fun replaceTransferData(
        financeValues: List<ClubFinance>,
        transactionValues: List<FinanceTransaction>
    ) {
        finances.clear()
        transactions.clear()
        financeValues.forEach(::saveFinance)
        transactionValues.forEach(::saveTransaction)
    }

    fun clearTransferData() {
        finances.clear()
        transactions.clear()
    }

    fun saveSponsorshipOffer(
        offer: SponsorshipOffer
    ) {
        sponsorshipOffers[offer.id] = offer
    }

    fun sponsorshipOffers(
        clubId: String
    ): List<SponsorshipOffer> =
        sponsorshipOffers.values.filter {
            it.clubId == clubId
        }

    fun saveActiveSponsorship(
        sponsorship: ActiveSponsorship
    ) {
        activeSponsorships[
            sponsorship.offerId
        ] = sponsorship
    }

    fun activeSponsorships(
        clubId: String
    ): List<ActiveSponsorship> =
        activeSponsorships.values.filter {
            it.clubId == clubId
        }
}
