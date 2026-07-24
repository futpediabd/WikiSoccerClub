package com.wikisoccerclub.ui.youth

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.youth.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class YouthAcademyUiState(
    val academy: YouthAcademy? = null,
    val trialProspects: List<YouthProspect> =
        emptyList(),
    val academyProspects: List<YouthProspect> =
        emptyList(),
    val lastIntake: YouthIntakeResult? = null,
    val error: String? = null
)

class YouthAcademyViewModel(
    private val repository:
        YouthAcademyRepository =
        YouthAcademyRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(YouthAcademyUiState())

    val uiState: StateFlow<YouthAcademyUiState> =
        _uiState.asStateFlow()

    fun configureAcademy(academy: YouthAcademy) {
        repository.saveAcademy(academy)
        refresh(academy.clubId)
    }

    fun runIntake(
        request: YouthIntakeRequest,
        availableBalance: Long
    ) {
        runCatching {
            val result =
                YouthAcademyEngine.generateIntake(
                    request
                )

            require(
                availableBalance >= result.totalCost
            ) {
                "Saldo insuficiente para realizar a peneira."
            }

            repository.saveIntake(result)
            result
        }.onSuccess { result ->
            _uiState.value =
                _uiState.value.copy(
                    trialProspects = result.prospects,
                    lastIntake = result,
                    error = null
                )
        }.onFailure {
            showError(it.message)
        }
    }

    fun signProspect(
        clubId: String,
        prospectId: String
    ) {
        val academy = repository.findAcademy(clubId)
            ?: return showError(
                "Academia não encontrada."
            )
        val prospect =
            repository.findProspect(prospectId)
                ?: return showError(
                    "Jogador não encontrado."
                )

        runCatching {
            YouthAcademyEngine.signProspect(
                academy = academy,
                prospect = prospect
            )
        }.onSuccess { (updatedAcademy, signed) ->
            repository.saveAcademy(updatedAcademy)
            repository.saveProspect(signed)
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun releaseProspect(
        clubId: String,
        prospectId: String
    ) {
        val academy = repository.findAcademy(clubId)
            ?: return showError(
                "Academia não encontrada."
            )
        val prospect =
            repository.findProspect(prospectId)
                ?: return showError(
                "Jogador não encontrado."
            )

        val (updatedAcademy, released) =
            YouthAcademyEngine.releaseProspect(
                academy = academy,
                prospect = prospect
            )

        repository.saveAcademy(updatedAcademy)
        repository.saveProspect(released)
        refresh(clubId)
    }

    fun promoteProspect(
        clubId: String,
        prospectId: String,
        seniorSquadSize: Int
    ) {
        val academy = repository.findAcademy(clubId)
            ?: return showError(
                "Academia não encontrada."
            )
        val prospect =
            repository.findProspect(prospectId)
                ?: return showError(
                    "Jogador não encontrado."
                )

        runCatching {
            YouthAcademyEngine.promoteProspect(
                academy = academy,
                prospect = prospect,
                seniorSquadSize = seniorSquadSize
            )
        }.onSuccess { (updatedAcademy, promoted) ->
            repository.saveAcademy(updatedAcademy)
            repository.saveProspect(promoted)
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun developAcademy(clubId: String) {
        val academy = repository.findAcademy(clubId)
            ?: return showError(
                "Academia não encontrada."
            )

        repository.academyProspects(clubId)
            .map {
                YouthDevelopmentEngine.developSeason(
                    prospect = it,
                    academy = academy
                )
            }
            .forEach(repository::saveProspect)

        refresh(clubId)
    }

    private fun refresh(clubId: String) {
        val academy =
            repository.findAcademy(clubId)

        _uiState.value = YouthAcademyUiState(
            academy = academy,
            trialProspects =
                repository.prospects().filter {
                    it.status ==
                        YouthProspectStatus.TRIAL
                },
            academyProspects =
                repository.academyProspects(clubId),
            lastIntake =
                repository.intakeHistory(clubId)
                    .firstOrNull()
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
