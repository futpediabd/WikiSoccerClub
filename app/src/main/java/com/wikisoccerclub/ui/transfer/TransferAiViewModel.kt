package com.wikisoccerclub.ui.transfer

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.transfer.*
import com.wikisoccerclub.core.transfer.TransferModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransferAiUiState(
    val decisions: List<AiTransferDecision> =
        emptyList(),
    val generatedOffers: List<TransferOffer> =
        emptyList(),
    val processing: Boolean = false,
    val error: String? = null
)

class TransferAiViewModel(
    private val repository: TransferAiRepository = TransferModule.ai,
    private val offerRepository: TransferOfferRepository = TransferModule.offers
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(TransferAiUiState())

    val uiState: StateFlow<TransferAiUiState> =
        _uiState.asStateFlow()

    fun configureMarket(
        clubs: List<AiClubProfile>,
        players: List<MarketPlayer>
    ) {
        repository.saveClubs(clubs)
        repository.savePlayers(players)
    }

    fun simulateMarket(
        maxTargetsPerClub: Int = 3
    ) {
        _uiState.value =
            _uiState.value.copy(
                processing = true,
                error = null
            )

        runCatching {
            repository.clearDecisions()

            repository.clubs().forEach { club ->
                TransferAiEngine.evaluateTargets(
                    club = club,
                    players = repository.players(),
                    maxTargets = maxTargetsPerClub
                ).forEach(repository::saveDecision)
            }

            val decisions = repository.decisions()
            val offers = decisions.mapIndexed {
                    index,
                    decision
                ->
                val player = repository.players()
                    .first {
                        it.playerId == decision.playerId
                    }

                TransferOffer(
                    id = "ia_${decision.clubId}_" +
                        "${decision.playerId}_$index",
                    playerId = decision.playerId,
                    sellingClubId =
                        player.clubId ?: "",
                    buyingClubId = decision.clubId,
                    value = decision.transferValue
                )
            }

            decisions to offers
        }.onSuccess { (decisions, offers) ->
            offers.forEach(offerRepository::save)
            _uiState.value = TransferAiUiState(
                decisions = decisions,
                generatedOffers = offers
            )
        }.onFailure {
            _uiState.value =
                _uiState.value.copy(
                    processing = false,
                    error = it.message
                )
        }
    }
}
