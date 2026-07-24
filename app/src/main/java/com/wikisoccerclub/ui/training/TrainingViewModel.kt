package com.wikisoccerclub.ui.training

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.training.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TrainingUiState(
    val profiles: List<PlayerTrainingProfile> =
        emptyList(),
    val staff: List<StaffMember> =
        emptyList(),
    val currentPlan: WeeklyTrainingPlan? = null,
    val lastResults: List<TrainingResult> =
        emptyList(),
    val monthlyStaffCost: Long = 0,
    val error: String? = null
)

class TrainingViewModel(
    private val repository: TrainingRepository =
        TrainingRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(TrainingUiState())

    val uiState: StateFlow<TrainingUiState> =
        _uiState.asStateFlow()

    fun configurePlayers(
        profiles: List<PlayerTrainingProfile>
    ) {
        repository.saveProfiles(profiles)
        refresh()
    }

    fun hireStaff(
        candidate: StaffMember,
        availableMonthlyBudget: Long,
        maximumStaff: Int = 12
    ) {
        val current = repository.allStaff()

        StaffEngine.canHire(
            currentStaff = current,
            candidate = candidate,
            availableMonthlyBudget =
                availableMonthlyBudget,
            maximumStaff = maximumStaff
        ).onSuccess {
            repository.saveStaff(candidate)
            refresh()
        }.onFailure {
            showError(it.message)
        }
    }

    fun dismissStaff(staffId: String) {
        repository.removeStaff(staffId)
        refresh()
    }

    fun createRecommendedPlan(
        clubId: String,
        seasonYear: Int,
        weekNumber: Int,
        matchesInWeek: Int,
        tacticalNeed: Boolean
    ) {
        val profiles =
            repository.allProfiles()
        val averageFatigue =
            if (profiles.isEmpty()) 0
            else profiles.map {
                it.fatigue
            }.average().toInt()

        runCatching {
            TrainingPlanEngine.recommendedPlan(
                clubId = clubId,
                seasonYear = seasonYear,
                weekNumber = weekNumber,
                matchesInWeek = matchesInWeek,
                averageFatigue = averageFatigue,
                tacticalNeed = tacticalNeed
            )
        }.onSuccess {
            repository.savePlan(it)
            refresh(it)
        }.onFailure {
            showError(it.message)
        }
    }

    fun executeSession(
        session: TrainingSession
    ) {
        val staff = repository.allStaff()
        val targets =
            if (
                session.isIndividual &&
                session.playerId != null
            ) {
                listOfNotNull(
                    repository.findProfile(
                        session.playerId
                    )
                )
            } else {
                repository.allProfiles()
            }

        val results = mutableListOf<TrainingResult>()

        runCatching {
            targets.forEach { profile ->
                val result =
                    TrainingEngine.executeSession(
                        profile = profile,
                        session = session,
                        staff = staff
                    )

                repository.saveResult(result)
                repository.saveProfile(
                    TrainingEngine.applyResult(
                        profile = profile,
                        result = result
                    )
                )
                results += result
            }
        }.onSuccess {
            _uiState.value =
                _uiState.value.copy(
                    lastResults = results
                )
            refresh(_uiState.value.currentPlan)
        }.onFailure {
            showError(it.message)
        }
    }

    private fun refresh(
        plan: WeeklyTrainingPlan? =
            _uiState.value.currentPlan
    ) {
        val staff = repository.allStaff()
        _uiState.value = TrainingUiState(
            profiles =
                repository.allProfiles(),
            staff = staff,
            currentPlan = plan,
            lastResults =
                _uiState.value.lastResults,
            monthlyStaffCost =
                StaffEngine.monthlyStaffCost(staff)
        )
    }

    private fun showError(message: String?) {
        _uiState.value = _uiState.value.copy(
            error = message ?: "Erro desconhecido."
        )
    }
}
