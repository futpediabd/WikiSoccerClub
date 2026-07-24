package com.wikisoccerclub.data.transfer

class LoanRepository {
    private val offers = linkedMapOf<String, LoanOffer>()
    private val activeLoans = linkedMapOf<String, ActiveLoan>()

    fun saveOffer(offer: LoanOffer) {
        offers[offer.id] = offer
    }

    fun saveOffers(values: List<LoanOffer>) {
        values.forEach(::saveOffer)
    }

    fun offers(): List<LoanOffer> = offers.values.toList()

    fun saveActiveLoan(loan: ActiveLoan) {
        activeLoans[loan.offerId] = loan
    }

    fun saveActiveLoans(values: List<ActiveLoan>) {
        values.forEach(::saveActiveLoan)
    }

    fun activeLoans(): List<ActiveLoan> = activeLoans.values.toList()

    fun activeByPlayer(playerId: String): ActiveLoan? =
        activeLoans.values.firstOrNull { it.playerId == playerId }

    fun removeActiveLoan(offerId: String) {
        activeLoans.remove(offerId)
    }

    fun replaceAll(
        loanOffers: List<LoanOffer>,
        loans: List<ActiveLoan>
    ) {
        offers.clear()
        activeLoans.clear()
        saveOffers(loanOffers)
        saveActiveLoans(loans)
    }

    fun clear() {
        offers.clear()
        activeLoans.clear()
    }
}
