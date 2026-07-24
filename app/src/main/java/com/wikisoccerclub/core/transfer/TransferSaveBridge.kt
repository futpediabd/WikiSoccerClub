package com.wikisoccerclub.core.transfer

import com.wikisoccerclub.data.save.TransferSaveState

object TransferSaveBridge {
    fun snapshot(): TransferSaveState = TransferSaveState(
        offers = TransferModule.offers.all(),
        contractOffers = TransferModule.contracts.offers(),
        contracts = TransferModule.contracts.contracts(),
        loanOffers = TransferModule.loans.offers(),
        activeLoans = TransferModule.loans.activeLoans(),
        clubs = TransferModule.clubs.all(),
        history = TransferModule.history.all(),
        news = TransferModule.news.allNews(),
        audit = TransferModule.news.allAudit(),
        finances = TransferModule.finances.allFinances(),
        financeTransactions = TransferModule.finances.allTransactions()
    )

    fun restore(state: TransferSaveState) {
        TransferModule.offers.replaceAll(state.offers)
        TransferModule.contracts.replaceAll(
            contractOffers = state.contractOffers,
            playerContracts = state.contracts
        )
        TransferModule.loans.replaceAll(
            loanOffers = state.loanOffers,
            loans = state.activeLoans
        )
        TransferModule.clubs.replaceAll(state.clubs)
        TransferModule.history.replaceAll(state.history)
        TransferModule.news.replaceAll(state.news, state.audit)
        TransferModule.finances.replaceTransferData(
            state.finances,
            state.financeTransactions
        )
    }

    fun clear() {
        TransferModule.offers.clear()
        TransferModule.contracts.clear()
        TransferModule.loans.clear()
        TransferModule.clubs.clear()
        TransferModule.history.clear()
        TransferModule.news.clear()
        TransferModule.finances.clearTransferData()
    }
}
