package com.wikisoccerclub.ui.youth

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.youth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class YouthIntakeUiState(
    val lastIntake:
        YouthIntakeResult? = null,
    val intakeHistory:
        List<YouthIntakeResult> = emptyList(),
    val projections:
        List<YouthDevelopmentProjection> =
        emptyList(),
    val error: String? = null
)

class YouthIntakeViewModel(
    private val academyRepository:
        YouthRepository = YouthRepository(),
    private val intakeRepository:
        YouthIntakeRepository =
        YouthIntakeRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            YouthIntakeUiState()
        )

    val uiState:
        StateFlow<YouthIntakeUiState> =
        _uiState.asStateFlow()

    fun generateAnnualIntake(
        academy: YouthAcademy,
        config: YouthIntakeConfig,
        namePools: List<YouthNamePool>
    ) {
        runCatching {
            YouthGenerationEngine
                .generateIntake(
                    academy = academy,
                    config = config,
                    namePools = namePools
                )
        }.onSuccess { result ->
            academyRepository.saveAcademy(
                academy
            )
            academyRepository.savePlayers(
                result.players
            )
            intakeRepository.saveIntake(
                result
            )

            val projections =
                result.players.map {
                    YouthDevelopmentEngine
                        .project(
                            player = it,
                            academy = academy
                        )
                }

            intakeRepository.saveProjections(
                projections
            )

            _uiState.value =
                _uiState.value.copy(
                    lastIntake = result,
                    intakeHistory =
                        intakeRepository
                            .allIntakes(
                                academy.clubId
                            ),
                    projections =
                        intakeRepository
                            .allProjections(),
                    error = null
                )
        }.onFailure {
            showError(it.message)
        }
    }

    fun runQuarterlyReview(
        clubId: String
    ) {
        val academy =
            academyRepository
                .findAcademy(clubId)
                ?: return showError(
                    "Academia não encontrada."
                )

        val updated =
            academyRepository
                .allPlayers()
                .map {
                    YouthDevelopmentEngine
                        .applyQuarterlyReview(
                            player = it,
                            academy = academy
                        )
                }

        academyRepository.savePlayers(
            updated
        )

        val projections =
            updated.map {
                YouthDevelopmentEngine
                    .project(
                        player = it,
                        academy = academy
                    )
            }

        intakeRepository.saveProjections(
            projections
        )

        _uiState.value =
            _uiState.value.copy(
                projections =
                    intakeRepository
                        .allProjections(),
                error = null
            )
    }

    fun applyBirthdays() {
        val updated =
            academyRepository
                .allPlayers()
                .map {
                    YouthDevelopmentEngine
                        .applyBirthday(it)
                }

        academyRepository.savePlayers(
            updated
        )
    }

    private fun showError(
        message: String?
    ) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message ?: "Erro desconhecido."
            )
    }
}
