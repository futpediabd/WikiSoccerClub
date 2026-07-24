package com.wikisoccerclub.ui.transfer

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.transfer.*
import com.wikisoccerclub.core.transfer.TransferModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoanContractUiState(
    val loanOffers: List<LoanOffer> = emptyList(),
    val activeLoans: List<ActiveLoan> = emptyList(),
    val contractOffers: List<ContractOffer> = emptyList(),
    val contracts: List<PlayerContract> = emptyList(),
    val error: String? = null
)

class LoanContractViewModel(
    private val loanRepository: LoanRepository =
        TransferModule.loans,
    private val contractRepository: ContractRepository =
        TransferModule.contracts
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoanContractUiState())

    val uiState: StateFlow<LoanContractUiState> =
        _uiState.asStateFlow()

    fun createLoanOffer(offer: LoanOffer) {
        LoanEngine.validate(offer)
            .onSuccess {
                loanRepository.saveOffer(offer)
                refresh()
            }
            .onFailure {
                showError(it.message)
            }
    }

    fun acceptLoan(offerId: String) {
        val offer = loanRepository.findOffer(offerId)
            ?: return showError("Proposta não encontrada.")

        runCatching {
            LoanEngine.accept(offer)
        }.onSuccess { (accepted, active) ->
            loanRepository.saveOffer(accepted)
            loanRepository.saveActiveLoan(active)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun rejectLoan(offerId: String) {
        val offer = loanRepository.findOffer(offerId)
            ?: return showError("Proposta não encontrada.")

        loanRepository.saveOffer(
            LoanEngine.reject(offer)
        )
        refresh()
    }

    fun activatePurchaseOption(
        offerId: String,
        paidValue: Long
    ) {
        val offer = loanRepository.findOffer(offerId)
            ?: return showError("Empréstimo não encontrado.")

        runCatching {
            LoanEngine.activatePurchaseOption(
                offer = offer,
                paidValue = paidValue
            )
        }.onSuccess { purchased ->
            loanRepository.saveOffer(purchased)
            loanRepository.removeActiveLoan(offerId)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun createContractOffer(offer: ContractOffer) {
        ContractEngine.validate(offer)
            .onSuccess {
                contractRepository.saveOffer(offer)
                refresh()
            }
            .onFailure {
                showError(it.message)
            }
    }

    fun acceptContract(offer: ContractOffer) {
        runCatching {
            ContractEngine.accept(offer)
        }.onSuccess { (accepted, contract) ->
            contractRepository.saveOffer(accepted)
            contractRepository.saveContract(contract)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun rejectContract(offer: ContractOffer) {
        contractRepository.saveOffer(
            ContractEngine.reject(offer)
        )
        refresh()
    }

    fun load() = refresh()

    private fun refresh() {
        _uiState.value = LoanContractUiState(
            loanOffers = loanRepository.offers(),
            activeLoans = loanRepository.activeLoans(),
            contractOffers = contractRepository.offers(),
            contracts = contractRepository.contracts()
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
