package com.wikisoccerclub.data.training

import kotlin.math.roundToInt
import kotlin.random.Random

object TrainingEngine {

    fun executeSession(
        profile: PlayerTrainingProfile,
        session: TrainingSession,
        staff: List<StaffMember>,
        random: Random = Random.Default
    ): TrainingResult {
        require(profile.age in 14..50)
        require(profile.overall in 0..100)
        require(profile.potential in 0..100)
        require(session.durationMinutes in 15..240)

        val intensityValue = intensityValue(
            session.intensity
        )
        val coachingQuality =
            staffQuality(session.focus, staff)

        val ageModifier = when {
            profile.age <= 18 -> 1.35
            profile.age <= 23 -> 1.20
            profile.age <= 29 -> 1.00
            profile.age <= 33 -> 0.75
            else -> 0.45
        }

        val potentialRoom =
            (profile.potential - profile.overall)
                .coerceAtLeast(0)

        val developmentChance = (
            0.03 +
                coachingQuality / 500.0 +
                potentialRoom / 700.0
            ) * ageModifier * intensityValue

        val overallChange =
            if (
                potentialRoom > 0 &&
                random.nextDouble() < developmentChance
            ) 1 else 0

        val fitnessChange = when (session.focus) {
            TrainingFocus.FITNESS ->
                (3 * intensityValue).roundToInt()
            TrainingFocus.RECOVERY ->
                (4 / intensityValue)
                    .roundToInt()
            else ->
                (1 * intensityValue).roundToInt()
        }

        val sharpnessChange = when (session.focus) {
            TrainingFocus.RECOVERY -> 0
            else -> (
                2 * intensityValue +
                    coachingQuality / 40.0
                ).roundToInt()
        }

        val fatigueChange = when (session.focus) {
            TrainingFocus.RECOVERY ->
                -(5 + coachingQuality / 20)
            else -> (
                3 * intensityValue +
                    session.durationMinutes / 60.0
                ).roundToInt()
        }

        val moraleChange =
            if (
                profile.preferredFocus ==
                    session.focus
            ) 2 else if (
                session.intensity ==
                    TrainingIntensity.VERY_HIGH
            ) -2 else 0

        val injuryRiskModifier = (
            1.0 +
                intensityValue / 4.0 +
                profile.fatigue / 200.0 +
                profile.injuryProneness / 300.0
            ).coerceIn(0.7, 2.5)

        return TrainingResult(
            playerId = profile.playerId,
            overallChange = overallChange,
            fitnessChange = fitnessChange,
            sharpnessChange = sharpnessChange,
            fatigueChange = fatigueChange,
            moraleChange = moraleChange,
            injuryRiskModifier =
                injuryRiskModifier,
            message = messageFor(
                session.focus,
                overallChange
            )
        )
    }

    fun applyResult(
        profile: PlayerTrainingProfile,
        result: TrainingResult
    ): PlayerTrainingProfile =
        profile.copy(
            overall = (
                profile.overall +
                    result.overallChange
                ).coerceAtMost(profile.potential),
            fitness = (
                profile.fitness +
                    result.fitnessChange
                ).coerceIn(0, 100),
            sharpness = (
                profile.sharpness +
                    result.sharpnessChange
                ).coerceIn(0, 100),
            fatigue = (
                profile.fatigue +
                    result.fatigueChange
                ).coerceIn(0, 100),
            morale = (
                profile.morale +
                    result.moraleChange
                ).coerceIn(0, 100)
        )

    private fun intensityValue(
        intensity: TrainingIntensity
    ): Double = when (intensity) {
        TrainingIntensity.VERY_LOW -> 0.55
        TrainingIntensity.LOW -> 0.75
        TrainingIntensity.NORMAL -> 1.0
        TrainingIntensity.HIGH -> 1.25
        TrainingIntensity.VERY_HIGH -> 1.50
    }

    private fun staffQuality(
        focus: TrainingFocus,
        staff: List<StaffMember>
    ): Int {
        if (staff.isEmpty()) return 20

        val relevant = staff.filter {
            when (focus) {
                TrainingFocus.FITNESS,
                TrainingFocus.RECOVERY ->
                    it.role ==
                        StaffRole.FITNESS_COACH ||
                        it.role ==
                            StaffRole.PHYSIOTHERAPIST
                TrainingFocus.GOALKEEPING ->
                    it.role ==
                        StaffRole.GOALKEEPING_COACH
                TrainingFocus.ATTACKING ->
                    it.role ==
                        StaffRole.ATTACKING_COACH
                TrainingFocus.DEFENDING ->
                    it.role ==
                        StaffRole.DEFENDING_COACH
                TrainingFocus.TACTICS,
                TrainingFocus.POSSESSION,
                TrainingFocus.SET_PIECES ->
                    it.role ==
                        StaffRole.TACTICAL_COACH ||
                        it.role ==
                            StaffRole.ASSISTANT_MANAGER
            }
        }

        val source =
            if (relevant.isEmpty()) staff else relevant

        return source.maxOf {
            when (focus) {
                TrainingFocus.FITNESS,
                TrainingFocus.RECOVERY ->
                    maxOf(it.fitness, it.medical)
                TrainingFocus.TACTICS,
                TrainingFocus.POSSESSION,
                TrainingFocus.SET_PIECES ->
                    maxOf(
                        it.coaching,
                        it.tacticalKnowledge
                    )
                else -> it.coaching
            }
        }.coerceIn(0, 100)
    }

    private fun messageFor(
        focus: TrainingFocus,
        overallChange: Int
    ): String =
        if (overallChange > 0) {
            "O jogador evoluiu após treino de " +
                focus.name.lowercase() + "."
        } else {
            "Sessão de ${focus.name.lowercase()} concluída."
        }
}
