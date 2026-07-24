package com.wikisoccerclub.ui.stadium

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.stadium.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class StadiumUiState(
    val stadium: Stadium? = null,
    val activeUpgrades: List<StadiumUpgrade> =
        emptyList(),
    val lastAttendance:
        MatchAttendanceResult? = null,
    val error: String? = null
)

class StadiumViewModel(
    private val repository: StadiumRepository =
        StadiumRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(StadiumUiState())

    val uiState: StateFlow<StadiumUiState> =
        _uiState.asStateFlow()

    fun configure(stadium: Stadium) {
        repository.saveStadium(stadium)
        refresh(stadium.id)
    }

    fun calculateAttendance(
        input: MatchAttendanceInput
    ) {
        runCatching {
            AttendanceEngine.calculate(input)
        }.onSuccess {
            repository.saveAttendance(it)
            _uiState.value =
                _uiState.value.copy(
                    lastAttendance = it,
                    error = null
                )
        }.onFailure {
            showError(it.message)
        }
    }

    fun startUpgrade(
        facility: StadiumFacility,
        targetLevel: Int,
        currentDay: Int,
        availableBalance: Long
    ): Long {
        val stadium =
            _uiState.value.stadium
                ?: run {
                    showError(
                        "Estádio não configurado."
                    )
                    return availableBalance
                }

        return runCatching {
            val upgrade =
                StadiumUpgradeEngine.createUpgrade(
                    stadium = stadium,
                    facility = facility,
                    targetLevel = targetLevel,
                    currentDay = currentDay
                )

            require(
                upgrade.cost <= availableBalance
            ) {
                "Saldo insuficiente para iniciar a obra."
            }

            repository.saveUpgrade(upgrade)
            refresh(stadium.id)
            availableBalance - upgrade.cost
        }.getOrElse {
            showError(it.message)
            availableBalance
        }
    }

    fun advanceDay(currentDay: Int) {
        val stadium =
            _uiState.value.stadium ?: return

        repository.activeUpgrades(stadium.id)
            .filter {
                StadiumUpgradeEngine.canComplete(
                    it,
                    currentDay
                )
            }
            .forEach { upgrade ->
                val updated =
                    StadiumUpgradeEngine.complete(
                        stadium =
                            repository.findStadium(
                                stadium.id
                            ) ?: stadium,
                        upgrade = upgrade,
                        currentDay = currentDay
                    )

                repository.saveStadium(updated)
                repository.markUpgradeCompleted(
                    upgrade.id
                )
            }

        refresh(stadium.id)
    }

    private fun refresh(stadiumId: String) {
        _uiState.value =
            _uiState.value.copy(
                stadium =
                    repository.findStadium(stadiumId),
                activeUpgrades =
                    repository.activeUpgrades(
                        stadiumId
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
