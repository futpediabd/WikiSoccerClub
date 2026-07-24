package com.wikisoccerclub.data.training

object TrainingPlanEngine {

    fun validate(
        plan: WeeklyTrainingPlan
    ): Result<Unit> = runCatching {
        require(plan.weekNumber >= 1)
        require(plan.restDays in 0..7)
        require(plan.sessions.size <= 14) {
            "O plano semanal aceita no máximo 14 sessões."
        }
        require(
            plan.sessions.all {
                it.clubId == plan.clubId &&
                    it.seasonYear ==
                        plan.seasonYear
            }
        ) {
            "Todas as sessões devem pertencer ao mesmo clube e temporada."
        }
    }

    fun recommendedPlan(
        clubId: String,
        seasonYear: Int,
        weekNumber: Int,
        matchesInWeek: Int,
        averageFatigue: Int,
        tacticalNeed: Boolean
    ): WeeklyTrainingPlan {
        require(matchesInWeek in 0..3)
        require(averageFatigue in 0..100)

        val sessions =
            mutableListOf<TrainingSession>()

        var day = (weekNumber - 1) * 7 + 1

        if (matchesInWeek >= 2 ||
            averageFatigue >= 70
        ) {
            sessions += session(
                clubId,
                seasonYear,
                day,
                TrainingFocus.RECOVERY,
                TrainingIntensity.LOW
            )
            sessions += session(
                clubId,
                seasonYear,
                day + 2,
                TrainingFocus.TACTICS,
                TrainingIntensity.LOW
            )
        } else {
            sessions += session(
                clubId,
                seasonYear,
                day,
                TrainingFocus.FITNESS,
                TrainingIntensity.NORMAL
            )
            sessions += session(
                clubId,
                seasonYear,
                day + 1,
                if (tacticalNeed) {
                    TrainingFocus.TACTICS
                } else {
                    TrainingFocus.ATTACKING
                },
                TrainingIntensity.NORMAL
            )
            sessions += session(
                clubId,
                seasonYear,
                day + 3,
                TrainingFocus.DEFENDING,
                TrainingIntensity.NORMAL
            )
            sessions += session(
                clubId,
                seasonYear,
                day + 4,
                TrainingFocus.SET_PIECES,
                TrainingIntensity.LOW
            )
        }

        return WeeklyTrainingPlan(
            clubId = clubId,
            seasonYear = seasonYear,
            weekNumber = weekNumber,
            sessions = sessions,
            restDays = if (
                matchesInWeek >= 2 ||
                averageFatigue >= 70
            ) 3 else 2
        )
    }

    private fun session(
        clubId: String,
        seasonYear: Int,
        day: Int,
        focus: TrainingFocus,
        intensity: TrainingIntensity
    ): TrainingSession =
        TrainingSession(
            id =
                "treino_${clubId}_${seasonYear}_${day}_${focus.name}",
            clubId = clubId,
            seasonYear = seasonYear,
            day = day,
            focus = focus,
            intensity = intensity,
            durationMinutes =
                if (
                    focus ==
                        TrainingFocus.RECOVERY
                ) 45 else 90
        )
}
