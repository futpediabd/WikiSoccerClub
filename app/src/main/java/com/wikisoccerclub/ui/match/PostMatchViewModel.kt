package com.wikisoccerclub.ui.match

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.match.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PostMatchUiState(
    val result: CompletedMatchResult? = null,
    val saved: Boolean = false,
    val error: String? = null
)

class PostMatchViewModel(
    private val repository: MatchResultRepository = MatchResultRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostMatchUiState())
    val uiState: StateFlow<PostMatchUiState> = _uiState.asStateFlow()

    fun finishMatch(
        matchId: String,
        session: LiveMatchSession
    ) {
        runCatching {
            MatchResultEngine.buildResult(
                matchId = matchId,
                session = session
            )
        }.onSuccess { result ->
            repository.save(result)
            _uiState.value = PostMatchUiState(
                result = result,
                saved = true
            )
        }.onFailure { throwable ->
            _uiState.value = PostMatchUiState(
                error = throwable.message ?: "Não foi possível salvar a partida."
            )
        }
    }

    fun load(matchId: String) {
        val result = repository.findByMatchId(matchId)

        _uiState.value = PostMatchUiState(
            result = result,
            saved = result != null,
            error = if (result == null) {
                "Resultado não encontrado."
            } else {
                null
            }
        )
    }
}
