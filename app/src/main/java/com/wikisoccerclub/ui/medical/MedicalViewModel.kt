package com.wikisoccerclub.ui.medical

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.medical.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MedicalUiState(
    val activeInjuries: List<Injury> = emptyList(),
    val disciplines: List<PlayerDiscipline> =
        emptyList(),
    val error: String? = null
)

class MedicalViewModel(
    private val repository: MedicalRepository =
        MedicalRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MedicalUiState())

    val uiState: StateFlow<MedicalUiState> =
        _uiState.asStateFlow()

    fun saveProfile(profile: PlayerMedicalProfile) {
        repository.saveProfile(profile)
    }

    fun evaluateMatchInjury(
        playerId: String,
        minutesPlayed: Int,
        matchIntensity: Int,
        seasonYear: Int,
        currentDay: Int
    ) {
        val profile = repository.findProfile(playerId)
            ?: return showError(
                "Perfil médico do jogador não encontrado."
            )

        runCatching {
            InjuryEngine.rollInjury(
                profile = profile,
                minutesPlayed = minutesPlayed,
                matchIntensity = matchIntensity,
                seasonYear = seasonYear,
                currentDay = currentDay
            )
        }.onSuccess { injury ->
            injury?.let(repository::saveInjury)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun advanceRecovery(
        days: Int,
        medicalDepartmentLevel: Int
    ) {
        runCatching {
            repository.activeInjuries()
                .forEach { injury ->
                    repository.saveInjury(
                        InjuryEngine.advanceRecovery(
                            injury = injury,
                            days = days,
                            medicalDepartmentLevel =
                                medicalDepartmentLevel
                        )
                    )
                }
        }.onSuccess {
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun registerYellowCard(
        playerId: String,
        competitionId: String,
        accumulationLimit: Int = 3
    ) {
        val current =
            repository.findDiscipline(
                playerId,
                competitionId
            ) ?: PlayerDiscipline(
                playerId = playerId,
                competitionId = competitionId
            )

        runCatching {
            DisciplineEngine.registerYellowCard(
                discipline = current,
                accumulationLimit = accumulationLimit
            )
        }.onSuccess { (updated, suspension) ->
            repository.saveDiscipline(updated)
            suspension?.let(
                repository::saveSuspension
            )
            refresh(competitionId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun registerRedCard(
        playerId: String,
        competitionId: String,
        suspensionMatches: Int = 1
    ) {
        val current =
            repository.findDiscipline(
                playerId,
                competitionId
            ) ?: PlayerDiscipline(
                playerId = playerId,
                competitionId = competitionId
            )

        runCatching {
            DisciplineEngine.registerRedCard(
                discipline = current,
                suspensionMatches = suspensionMatches
            )
        }.onSuccess { (updated, suspension) ->
            repository.saveDiscipline(updated)
            repository.saveSuspension(suspension)
            refresh(competitionId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun serveSuspension(
        playerId: String,
        competitionId: String
    ) {
        val discipline =
            repository.findDiscipline(
                playerId,
                competitionId
            ) ?: return

        val suspension =
            repository.activeSuspensions(playerId)
                .firstOrNull {
                    it.competitionId == competitionId
                } ?: return

        val (updatedDiscipline, updatedSuspension) =
            DisciplineEngine.serveSuspensionMatch(
                discipline = discipline,
                suspension = suspension
            )

        repository.saveDiscipline(updatedDiscipline)
        repository.saveSuspension(updatedSuspension)
        refresh(competitionId)
    }

    private fun refresh(
        competitionId: String? = null
    ) {
        _uiState.value = MedicalUiState(
            activeInjuries =
                repository.activeInjuries(),
            disciplines = if (
                competitionId == null
            ) {
                _uiState.value.disciplines
            } else {
                repository.disciplinesByCompetition(
                    competitionId
                )
            }
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
