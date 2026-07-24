package com.wikisoccerclub.ui.transfer

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.data.transfer.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransferWindowUiState(
    val currentDate: CareerDate = CareerDate(2026, 1, 1),
    val windows: List<TransferWindow> = emptyList(),
    val targets: List<TransferTarget> = emptyList(),
    val events: List<TransferWindowEvent> = emptyList(),
    val isMarketOpen: Boolean = false,
    val statusMessage: String = ""
)

class TransferWindowViewModel(
    private val repo: TransferRepository = TransferModule.targets,
    private val windowRepository: TransferWindowRepository = TransferModule.windows
) : ViewModel() {
    private val _ui = MutableStateFlow(TransferWindowUiState())
    val ui: StateFlow<TransferWindowUiState> = _ui.asStateFlow()

    fun load(year: Int) {
        val date = windowRepository.currentDate().takeIf { it.year == year } ?: CareerDate(year, 1, 1)
        refresh(date)
    }

    fun configureSeason(config: TransferSeasonWindowConfig) {
        windowRepository.configure(config)
        refresh(windowRepository.currentDate())
    }

    fun onCareerDateChanged(date: CareerDate) {
        windowRepository.updateCareerDate(date)
        refresh(date)
    }

    fun addTarget(target: TransferTarget): Result<Unit> = runCatching {
        windowRepository.requireOpen()
        repo.save(target)
        refresh(windowRepository.currentDate())
    }

    private fun refresh(date: CareerDate) {
        val status = windowRepository.status(date)
        _ui.value = TransferWindowUiState(
            currentDate = date,
            windows = status.windows,
            targets = repo.all(),
            events = windowRepository.allEvents(),
            isMarketOpen = status.isOpen,
            statusMessage = status.activeWindow?.let {
                "Mercado aberto até ${it.endDate.formatted()}"
            } ?: status.nextWindow?.let {
                "Mercado fechado • próxima abertura em ${it.startDate.formatted()}"
            } ?: "Mercado fechado nesta temporada"
        )
    }
}
