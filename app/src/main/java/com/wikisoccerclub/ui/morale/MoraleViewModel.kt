package com.wikisoccerclub.ui.morale

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.morale.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MoraleUiState(
    val playerMorales: List<PlayerMorale> =
        emptyList(),
    val concerns: List<PlayerConcern> =
        emptyList(),
    val atmosphere: SquadAtmosphere? = null,
    val error: String? = null
)

class MoraleViewModel(
    private val repository: MoraleRepository =
        MoraleRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MoraleUiState())

    val uiState: StateFlow<MoraleUiState> =
        _uiState.asStateFlow()

    fun configure(players: List<PlayerMorale>) {
        repository.saveMorales(players)
        refresh()
    }

    fun registerMatch(
        playerId: String,
        won: Boolean,
        drew: Boolean,
        started: Boolean,
        minutes: Int,
        rating: Double
    ) {
        val current =
            repository.findMorale(playerId)
                ?: return showError(
                    "Moral do jogador não encontrada."
                )

        runCatching {
            MoraleEngine.afterMatch(
                morale = current,
                won = won,
                drew = drew,
                startedMatch = started,
                minutesPlayed = minutes,
                playerRating = rating
            )
        }.onSuccess {
            repository.saveMorale(it)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun applyTeamTalk(
        tone: TeamTalkTone,
        opponentStrengthDifference: Int
    ) {
        repository.allMorales().forEach { morale ->
            val result = TeamTalkEngine.beforeMatch(
                tone = tone,
                playerMorale = morale.morale,
                opponentStrengthDifference =
                    opponentStrengthDifference
            )

            repository.saveMorale(
                MoraleEngine.applyTeamTalk(
                    morale = morale,
                    result = result
                )
            )
        }
        refresh()
    }

    fun detectConcerns(currentDay: Int) {
        repository.allMorales()
            .mapNotNull {
                ConcernEngine.detectPlayingTimeConcern(
                    morale = it,
                    currentDay = currentDay
                )
            }
            .forEach(repository::saveConcern)

        refresh()
    }

    fun resolveConcern(
        concernId: String,
        fulfilled: Boolean
    ) {
        val concern =
            repository.findConcern(concernId)
                ?: return showError(
                    "Preocupação não encontrada."
                )

        val updated =
            ConcernEngine.resolve(
                concern = concern,
                fulfilled = fulfilled
            )

        repository.saveConcern(updated)

        val morale =
            repository.findMorale(updated.playerId)

        morale?.let {
            repository.saveMorale(
                it.copy(
                    morale = (
                        it.morale +
                            ConcernEngine
                                .moraleImpact(updated)
                        ).coerceIn(0, 100)
                )
            )
        }

        refresh()
    }

    fun calculateAtmosphere(
        clubId: String,
        captainLeadership: Int,
        recentResultsScore: Int
    ) {
        runCatching {
            SquadAtmosphereEngine.calculate(
                clubId = clubId,
                playerMorales =
                    repository.allMorales(),
                captainLeadership =
                    captainLeadership,
                recentResultsScore =
                    recentResultsScore
            )
        }.onSuccess {
            repository.saveAtmosphere(it)
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    private fun refresh(clubId: String? = null) {
        _uiState.value = MoraleUiState(
            playerMorales =
                repository.allMorales(),
            concerns =
                repository.activeConcerns(),
            atmosphere =
                clubId?.let {
                    repository.findAtmosphere(it)
                } ?: _uiState.value.atmosphere
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
