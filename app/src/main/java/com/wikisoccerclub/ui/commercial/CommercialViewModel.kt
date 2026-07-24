package com.wikisoccerclub.ui.commercial

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.commercial.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CommercialUiState(
    val offers: List<SponsorOffer> =
        emptyList(),
    val contracts: List<SponsorContract> =
        emptyList(),
    val merchandiseHistory:
        List<MerchandiseResult> = emptyList(),
    val summary: CommercialSummary? = null,
    val lastFinancialChange: Long = 0,
    val error: String? = null
)

class CommercialViewModel(
    private val repository: CommercialRepository =
        CommercialRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CommercialUiState())

    val uiState: StateFlow<CommercialUiState> =
        _uiState.asStateFlow()

    fun generateOffers(
        clubId: String,
        clubName: String,
        clubReputation: Int,
        leagueReputation: Int,
        recentTitles: Int,
        seasonYear: Int
    ) {
        runCatching {
            SponsorOfferEngine.generateOffers(
                clubId = clubId,
                clubName = clubName,
                clubReputation =
                    clubReputation,
                leagueReputation =
                    leagueReputation,
                recentTitles = recentTitles,
                seasonYear = seasonYear
            )
        }.onSuccess {
            repository.saveOffers(it)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun acceptOffer(
        offerId: String,
        clubId: String,
        clubReputation: Int,
        currentSeason: Int
    ) {
        val offer =
            repository.availableOffers()
                .firstOrNull { it.id == offerId }
                ?: return showError(
                    "Oferta não encontrada."
                )

        SponsorOfferEngine.canAccept(
            offer = offer,
            clubReputation = clubReputation,
            activeContracts =
                repository.activeContracts()
        ).onSuccess {
            val contract =
                SponsorOfferEngine.accept(
                    offer = offer,
                    clubId = clubId,
                    currentSeason =
                        currentSeason
                )

            repository.saveContract(contract)
            repository.removeOffer(offerId)
            _uiState.value =
                _uiState.value.copy(
                    lastFinancialChange =
                        contract.signingBonus
                )
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun updateObjective(
        contractId: String,
        type: SponsorObjectiveType,
        value: Int,
        absoluteValue: Boolean = false
    ) {
        val contract =
            repository.findContract(contractId)
                ?: return showError(
                    "Contrato não encontrado."
                )

        runCatching {
            SponsorObjectiveEngine.updateProgress(
                contract = contract,
                type = type,
                value = value,
                absoluteValue = absoluteValue
            )
        }.onSuccess {
            repository.saveContract(it)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun settleSeason(seasonYear: Int) {
        var financialChange = 0L

        repository.activeContracts()
            .forEach { contract ->
                val updated =
                    SponsorObjectiveEngine
                        .settleSeason(
                            contract,
                            seasonYear
                        )

                financialChange +=
                    SponsorObjectiveEngine
                        .financialSettlement(
                            before = contract,
                            after = updated
                        )

                repository.saveContract(updated)
            }

        _uiState.value =
            _uiState.value.copy(
                lastFinancialChange =
                    financialChange
            )
        refresh()
    }

    fun calculateMerchandise(
        profile: MerchandiseProfile,
        seasonYear: Int
    ) {
        runCatching {
            MerchandiseEngine.calculateSeason(
                profile = profile,
                seasonYear = seasonYear
            )
        }.onSuccess {
            repository.saveMerchandiseResult(it)
            _uiState.value =
                _uiState.value.copy(
                    lastFinancialChange =
                        it.netRevenue
                )
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    private fun refresh() {
        val contracts =
            repository.allContracts()
        val merchandise =
            repository.merchandiseHistory()

        _uiState.value =
            _uiState.value.copy(
                offers =
                    repository.availableOffers(),
                contracts = contracts,
                merchandiseHistory =
                    merchandise,
                summary =
                    CommercialSummaryEngine.build(
                        contracts,
                        merchandise
                    ),
                error = null
            )
    }

    private fun showError(message: String?) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message ?: "Erro desconhecido."
            )
    }
}
