package com.wikisoccerclub.ui.competition

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.data.match.CompletedMatchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GroupStageUiState(
    val progress: GroupStageProgress? = null,
    val error: String? = null
)

class GroupStageViewModel(
    private val repository: GroupStageRepository =
        GroupStageRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupStageUiState())
    val uiState: StateFlow<GroupStageUiState> =
        _uiState.asStateFlow()

    fun initialize(
        competitionId: String,
        teamIds: List<String>,
        teamNames: Map<String, String>,
        config: GroupStageConfig
    ) {
        runCatching {
            GroupStageFactory.create(
                competitionId = competitionId,
                teamIds = teamIds,
                teamNames = teamNames,
                config = config
            )
        }.onSuccess { progress ->
            repository.save(progress)
            _uiState.value = GroupStageUiState(progress = progress)
        }.onFailure { error ->
            _uiState.value = GroupStageUiState(
                error = error.message
            )
        }
    }

    fun applyResult(
        competitionId: String,
        result: CompletedMatchResult,
        qualifiedPerGroup: Int
    ) {
        val current = repository.find(competitionId)

        if (current == null) {
            _uiState.value = GroupStageUiState(
                error = "Fase de grupos não encontrada."
            )
            return
        }

        val updated = GroupStageEngine.applyResult(
            progress = current,
            result = result,
            qualifiedPerGroup = qualifiedPerGroup
        )

        repository.save(updated)
        _uiState.value = GroupStageUiState(progress = updated)
    }

    fun load(competitionId: String) {
        val progress = repository.find(competitionId)
        _uiState.value = GroupStageUiState(
            progress = progress,
            error = if (progress == null) {
                "Fase de grupos não encontrada."
            } else {
                null
            }
        )
    }
}
