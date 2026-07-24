package com.wikisoccerclub.ui.youth

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.youth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class YouthTryoutControlUiState(
    val clubBalance: Long = 0L,
    val availability:
        YouthTryoutAvailability? = null,
    val signedPlayers:
        List<YouthSignedPlayer> = emptyList(),
    val error: String? = null
)

class YouthTryoutControlViewModel(
    private val repository:
        YouthTryoutControlRepository =
        YouthTryoutControlRepository(),
    private val settings:
        YouthTryoutSettings =
        YouthTryoutSettings()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            YouthTryoutControlUiState()
        )

    val uiState:
        StateFlow<YouthTryoutControlUiState> =
        _uiState.asStateFlow()

    fun loadAvailability(
        clubId: String,
        position: YouthPositionFilter,
        currentDay: Int,
        clubBalance: Long
    ) {
        val availability =
            YouthTryoutControlEngine
                .availability(
                    position = position,
                    currentDay = currentDay,
                    control =
                        repository.controlFor(
                            clubId
                        ),
                    settings = settings
                )

        _uiState.value =
            _uiState.value.copy(
                clubBalance = clubBalance,
                availability = availability,
                signedPlayers =
                    repository.allSignedPlayers(),
                error = null
            )
    }

    fun confirmTryoutPayment(
        clubId: String,
        currentDay: Int
    ): Long? {
        val availability =
            _uiState.value.availability
                ?: return showErrorAndNull(
                    "Disponibilidade não carregada."
                )

        if (!availability.available) {
            return showErrorAndNull(
                availability.message
            )
        }

        return runCatching {
            YouthTryoutControlEngine
                .updatedBalanceAfterTryout(
                    clubBalance =
                        _uiState.value.clubBalance,
                    cost = availability.cost
                )
        }.onSuccess { newBalance ->
            repository.markTryout(
                clubId = clubId,
                currentDay = currentDay
            )

            _uiState.value =
                _uiState.value.copy(
                    clubBalance = newBalance,
                    error = null
                )
        }.onFailure {
            showError(it.message)
        }.getOrNull()
    }

    fun saveContractedCandidate(
        candidate: YouthCandidate,
        contractYears: Int = 3
    ) {
        runCatching {
            YouthTryoutControlEngine
                .toSignedPlayer(
                    candidate = candidate,
                    contractYears =
                        contractYears
                )
        }.onSuccess {
            repository.saveSignedPlayer(it)

            _uiState.value =
                _uiState.value.copy(
                    signedPlayers =
                        repository
                            .allSignedPlayers(),
                    error = null
                )
        }.onFailure {
            showError(it.message)
        }
    }

    private fun showErrorAndNull(
        message: String
    ): Long? {
        showError(message)
        return null
    }

    private fun showError(
        message: String?
    ) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message
                        ?: "Erro desconhecido."
            )
    }
}
