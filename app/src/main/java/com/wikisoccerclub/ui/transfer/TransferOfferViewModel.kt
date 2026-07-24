package com.wikisoccerclub.ui.transfer

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.core.transfer.TransferWorkflowService
import com.wikisoccerclub.data.transfer.TransferOffer
import com.wikisoccerclub.data.transfer.TransferOfferRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransferOfferUiState(
    val offers: List<TransferOffer> = emptyList(),
    val error: String? = null
)

class TransferOfferViewModel(
    private val repo: TransferOfferRepository = TransferModule.offers,
    private val workflow: TransferWorkflowService = TransferModule.workflow
) : ViewModel() {
    private val _ui = MutableStateFlow(TransferOfferUiState(repo.all()))
    val ui: StateFlow<TransferOfferUiState> = _ui.asStateFlow()

    fun load() = refresh()

    fun create(offer: TransferOffer) {
        workflow.createOffer(offer)
            .onSuccess { refresh() }
            .onFailure(::showError)
    }

    fun accept(offerId: String) {
        workflow.acceptOffer(offerId)
            .onSuccess { refresh() }
            .onFailure(::showError)
    }

    fun reject(offerId: String) {
        workflow.rejectOffer(offerId)
            .onSuccess { refresh() }
            .onFailure(::showError)
    }

    fun counter(offerId: String, newValue: Long) {
        workflow.counterOffer(offerId, newValue)
            .onSuccess { refresh() }
            .onFailure(::showError)
    }

    private fun refresh() {
        _ui.value = TransferOfferUiState(offers = repo.all())
    }

    private fun showError(error: Throwable) {
        _ui.value = _ui.value.copy(error = error.message ?: "Erro na proposta.")
    }
}
