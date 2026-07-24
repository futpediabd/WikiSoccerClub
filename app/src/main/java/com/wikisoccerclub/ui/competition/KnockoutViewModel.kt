package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class KnockoutUiState(
    val progress: KnockoutCompetitionProgress? = null,
    val error: String? = null
)

class KnockoutViewModel(
    private val repository: KnockoutRepository =
        KnockoutRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(KnockoutUiState())
    val uiState: StateFlow<KnockoutUiState> =
        _uiState.asStateFlow()

    fun initialize(
        competitionId: String,
        teamIds: List<String>,
        initialRound: KnockoutRoundType,
        twoLegged: Boolean = true
    ) {
        runCatching {
            KnockoutFactory.create(
                competitionId = competitionId,
                teamIds = teamIds,
                initialRound = initialRound,
                twoLegged = twoLegged
            )
        }.onSuccess { progress ->
            repository.save(progress)
            _uiState.value = KnockoutUiState(
                progress = progress
            )
        }.onFailure { error ->
            _uiState.value = KnockoutUiState(
                error = error.message
            )
        }
    }

    fun applyResult(
        competitionId: String,
        result: KnockoutMatchResult
    ) {
        val current = repository.find(competitionId)

        if (current == null) {
            _uiState.value = KnockoutUiState(
                error = "Competição não encontrada."
            )
            return
        }

        val updated = KnockoutEngine.applyResult(
            progress = current,
            result = result
        )

        repository.save(updated)
        _uiState.value = KnockoutUiState(progress = updated)
    }

    fun load(competitionId: String) {
        val progress = repository.find(competitionId)
        _uiState.value = KnockoutUiState(
            progress = progress,
            error = if (progress == null) {
                "Competição não encontrada."
            } else {
                null
            }
        )
    }
}
