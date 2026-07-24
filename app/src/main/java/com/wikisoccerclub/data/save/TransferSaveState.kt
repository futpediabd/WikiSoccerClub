package com.wikisoccerclub.data.save

import com.wikisoccerclub.data.transfer.ActiveLoan
import com.wikisoccerclub.data.transfer.ClubTransferState
import com.wikisoccerclub.data.transfer.CompletedTransfer
import com.wikisoccerclub.data.transfer.ContractOffer
import com.wikisoccerclub.data.transfer.LoanOffer
import com.wikisoccerclub.data.transfer.PlayerContract
import com.wikisoccerclub.data.transfer.TransferOffer
import com.wikisoccerclub.data.transfer.TransferNewsItem
import com.wikisoccerclub.data.transfer.TransferAuditEvent
import com.wikisoccerclub.data.finance.ClubFinance
import com.wikisoccerclub.data.finance.FinanceTransaction

data class TransferSaveState(
    val offers: List<TransferOffer> = emptyList(),
    val contractOffers: List<ContractOffer> = emptyList(),
    val contracts: List<PlayerContract> = emptyList(),
    val loanOffers: List<LoanOffer> = emptyList(),
    val activeLoans: List<ActiveLoan> = emptyList(),
    val clubs: List<ClubTransferState> = emptyList(),
    val history: List<CompletedTransfer> = emptyList(),
    val news: List<TransferNewsItem> = emptyList(),
    val audit: List<TransferAuditEvent> = emptyList(),
    val finances: List<ClubFinance> = emptyList(),
    val financeTransactions: List<FinanceTransaction> = emptyList()
)
