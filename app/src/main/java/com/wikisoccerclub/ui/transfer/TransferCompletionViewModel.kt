package com.wikisoccerclub.ui.transfer

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.transfer.*
import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.core.transfer.TransferWorkflowService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransferCompletionUiState(
    val clubs: List<ClubTransferState> = emptyList(),
    val transfers: List<CompletedTransfer> = emptyList(),
    val lastTransfer: CompletedTransfer? = null,
    val error: String? = null
)

class TransferCompletionViewModel(
    private val clubRepository: ClubTransferRepository =
        TransferModule.clubs,
    private val historyRepository: TransferHistoryRepository =
        TransferModule.history,
    private val workflow: TransferWorkflowService = TransferModule.workflow
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(TransferCompletionUiState())

    val uiState: StateFlow<TransferCompletionUiState> =
        _uiState.asStateFlow()

    fun configureClubs(
        clubs: List<ClubTransferState>
    ) {
        clubRepository.saveAll(clubs)
        refresh()
    }

    fun completeTransfer(
        transferId: String,
        playerId: String,
        sellingClubId: String?,
        buyingClubId: String,
        transferValue: Long,
        contract: PlayerContract,
        seasonYear: Int
    ) {
        val result =
            TransferCompletionEngine.completePermanentTransfer(
                transferId = transferId,
                playerId = playerId,
                sellingClubId = sellingClubId,
                buyingClubId = buyingClubId,
                transferValue = transferValue,
                contract = contract,
                seasonYear = seasonYear,
                clubs = clubRepository.all()
            )

        consumeResult(result)
    }

    fun completeAcceptedOffer(
        offerId: String,
        seasonYear: Int
    ) {
        consumeResult(workflow.completeTransfer(offerId, seasonYear))
    }

    fun signFreeAgent(
        request: FreeAgentSigningRequest
    ) {
        val result = FreeAgentEngine.sign(
            request = request,
            clubs = clubRepository.all()
        )
        consumeResult(result)
    }

    fun completeLoanPurchase(
        loanOffer: LoanOffer,
        contract: PlayerContract,
        seasonYear: Int
    ) {
        val result =
            TransferCompletionEngine.completeLoanPurchase(
                loanOffer = loanOffer,
                contract = contract,
                seasonYear = seasonYear,
                clubs = clubRepository.all()
            )

        consumeResult(result)
    }

    private fun consumeResult(
        result: TransferCompletionResult
    ) {
        if (!result.successful) {
            _uiState.value = _uiState.value.copy(
                error = result.error
            )
            return
        }

        clubRepository.saveAll(result.updatedClubs)
        result.transfer?.let(historyRepository::save)

        _uiState.value = TransferCompletionUiState(
            clubs = clubRepository.all(),
            transfers = historyRepository.all(),
            lastTransfer = result.transfer
        )
    }

    fun load() = refresh()

    private fun refresh() {
        _uiState.value = TransferCompletionUiState(
            clubs = clubRepository.all(),
            transfers = historyRepository.all()
        )
    }
}
