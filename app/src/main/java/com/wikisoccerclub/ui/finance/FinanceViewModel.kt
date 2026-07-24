package com.wikisoccerclub.ui.finance

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.core.finance.FinanceModule
import com.wikisoccerclub.data.finance.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FinanceUiState(
    val finance: ClubFinance? = null,
    val transactions: List<FinanceTransaction> =
        emptyList(),
    val summaries: List<MonthlyFinanceSummary> =
        emptyList(),
    val sponsorshipOffers: List<SponsorshipOffer> =
        emptyList(),
    val activeSponsorships:
        List<ActiveSponsorship> = emptyList(),
    val error: String? = null
)

class FinanceViewModel(
    private val repository: FinanceRepository =
        FinanceModule.repository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(FinanceUiState())

    val uiState: StateFlow<FinanceUiState> =
        _uiState.asStateFlow()

    fun configure(finance: ClubFinance) {
        FinanceEngine.validateFinance(finance)
            .onSuccess {
                repository.saveFinance(finance)
                refresh(finance.clubId)
            }
            .onFailure {
                showError(it.message)
            }
    }

    fun addTransaction(
        transaction: FinanceTransaction
    ) {
        val finance =
            repository.findFinance(transaction.clubId)
                ?: return showError(
                    "Finanças do clube não encontradas."
                )

        runCatching {
            FinanceEngine.applyTransaction(
                finance = finance,
                transaction = transaction
            )
        }.onSuccess { updated ->
            repository.saveTransaction(transaction)
            repository.saveFinance(updated)
            refresh(transaction.clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun processMonth(
        clubId: String,
        seasonYear: Int,
        month: Int
    ) {
        val finance = repository.findFinance(clubId)
            ?: return showError(
                "Finanças do clube não encontradas."
            )

        val sponsorshipTransactions =
            repository.activeSponsorships(clubId)
                .filter {
                    seasonYear in it.startYear..it.endYear
                }
                .map {
                    SponsorshipEngine.monthlyTransaction(
                        sponsorship = it,
                        seasonYear = seasonYear,
                        month = month
                    )
                }

        runCatching {
            FinanceEngine.processMonth(
                finance = finance,
                seasonYear = seasonYear,
                month = month,
                extraTransactions =
                    sponsorshipTransactions
            )
        }.onSuccess { (updated, summary) ->
            repository.saveFinance(updated)
            repository.saveTransactions(
                FinanceEngine.createMonthlyExpenses(
                    finance = finance,
                    seasonYear = seasonYear,
                    month = month
                ) + sponsorshipTransactions
            )
            repository.saveSummary(summary)
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun addSponsorshipOffer(
        offer: SponsorshipOffer
    ) {
        SponsorshipEngine.validateOffer(offer)
            .onSuccess {
                repository.saveSponsorshipOffer(offer)
                refresh(offer.clubId)
            }
            .onFailure {
                showError(it.message)
            }
    }

    fun acceptSponsorship(
        offer: SponsorshipOffer
    ) {
        runCatching {
            SponsorshipEngine.accept(offer)
        }.onSuccess { (accepted, active) ->
            repository.saveSponsorshipOffer(accepted)
            repository.saveActiveSponsorship(active)

            if (accepted.signingBonus > 0) {
                addTransaction(
                    FinanceTransaction(
                        id = "assinatura_${accepted.id}",
                        clubId = accepted.clubId,
                        seasonYear = accepted.startYear,
                        month = 1,
                        type =
                            FinanceTransactionType.SPONSORSHIP,
                        description =
                            "Bônus de assinatura: ${accepted.sponsorName}",
                        amount = accepted.signingBonus
                    )
                )
            } else {
                refresh(accepted.clubId)
            }
        }.onFailure {
            showError(it.message)
        }
    }

    fun rejectSponsorship(
        offer: SponsorshipOffer
    ) {
        repository.saveSponsorshipOffer(
            SponsorshipEngine.reject(offer)
        )
        refresh(offer.clubId)
    }

    private fun refresh(clubId: String) {
        _uiState.value = FinanceUiState(
            finance = repository.findFinance(clubId),
            transactions =
                repository.transactionsByClub(clubId),
            summaries =
                repository.summariesByClub(clubId),
            sponsorshipOffers =
                repository.sponsorshipOffers(clubId),
            activeSponsorships =
                repository.activeSponsorships(clubId)
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
