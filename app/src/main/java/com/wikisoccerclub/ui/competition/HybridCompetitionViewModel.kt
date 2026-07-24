package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.data.match.CompletedMatchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HybridCompetitionUiState(
    val progress: HybridCompetitionProgress? = null,
    val error: String? = null
)

class HybridCompetitionViewModel(
    private val repository: HybridCompetitionRepository =
        HybridCompetitionRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(HybridCompetitionUiState())

    val uiState: StateFlow<HybridCompetitionUiState> =
        _uiState.asStateFlow()

    fun initialize(
        competitionId: String,
        teamIds: List<String>,
        teamNames: Map<String, String>,
        config: HybridCompetitionConfig =
            HybridCompetitionConfig()
    ) {
        runCatching {
            HybridCompetitionFactory.create(
                competitionId = competitionId,
                teamIds = teamIds,
                teamNames = teamNames,
                config = config
            )
        }.onSuccess { progress ->
            repository.save(progress)
            _uiState.value = HybridCompetitionUiState(
                progress = progress
            )
        }.onFailure { error ->
            _uiState.value = HybridCompetitionUiState(
                error = error.message
            )
        }
    }

    fun applyGroupResult(
        competitionId: String,
        result: CompletedMatchResult,
        config: HybridCompetitionConfig
    ) {
        val current = repository.find(competitionId)

        if (current == null) {
            _uiState.value = HybridCompetitionUiState(
                error = "Competição não encontrada."
            )
            return
        }

        val updated = HybridCompetitionEngine.applyGroupResult(
            progress = current,
            result = result,
            config = config
        )

        repository.save(updated)
        _uiState.value = HybridCompetitionUiState(
            progress = updated
        )
    }

    fun applyKnockoutResult(
        competitionId: String,
        result: KnockoutMatchResult
    ) {
        val current = repository.find(competitionId)

        if (current == null) {
            _uiState.value = HybridCompetitionUiState(
                error = "Competição não encontrada."
            )
            return
        }

        val updated = HybridCompetitionEngine.applyKnockoutResult(
            progress = current,
            result = result
        )

        repository.save(updated)
        _uiState.value = HybridCompetitionUiState(
            progress = updated
        )
    }

    fun load(competitionId: String) {
        val progress = repository.find(competitionId)

        _uiState.value = HybridCompetitionUiState(
            progress = progress,
            error = if (progress == null) {
                "Competição não encontrada."
            } else {
                null
            }
        )
    }
}
