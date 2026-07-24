package com.wikisoccerclub.data.training

enum class TrainingFocus {
    FITNESS,
    TACTICS,
    ATTACKING,
    DEFENDING,
    POSSESSION,
    SET_PIECES,
    GOALKEEPING,
    RECOVERY
}

enum class TrainingIntensity {
    VERY_LOW,
    LOW,
    NORMAL,
    HIGH,
    VERY_HIGH
}

enum class StaffRole {
    ASSISTANT_MANAGER,
    FITNESS_COACH,
    GOALKEEPING_COACH,
    ATTACKING_COACH,
    DEFENDING_COACH,
    TACTICAL_COACH,
    PHYSIOTHERAPIST,
    DOCTOR,
    SCOUT,
    YOUTH_COACH
}

data class StaffMember(
    val id: String,
    val name: String,
    val role: StaffRole,
    val coaching: Int,
    val tacticalKnowledge: Int,
    val fitness: Int,
    val medical: Int,
    val youthDevelopment: Int,
    val scouting: Int,
    val monthlySalary: Long,
    val contractEndYear: Int
)

data class PlayerTrainingProfile(
    val playerId: String,
    val age: Int,
    val overall: Int,
    val potential: Int,
    val fitness: Int,
    val sharpness: Int,
    val fatigue: Int,
    val morale: Int,
    val injuryProneness: Int,
    val preferredFocus: TrainingFocus? = null
)

data class TrainingSession(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val day: Int,
    val focus: TrainingFocus,
    val intensity: TrainingIntensity,
    val durationMinutes: Int,
    val isIndividual: Boolean = false,
    val playerId: String? = null
)

data class TrainingResult(
    val playerId: String,
    val overallChange: Int,
    val fitnessChange: Int,
    val sharpnessChange: Int,
    val fatigueChange: Int,
    val moraleChange: Int,
    val injuryRiskModifier: Double,
    val message: String
)

data class WeeklyTrainingPlan(
    val clubId: String,
    val seasonYear: Int,
    val weekNumber: Int,
    val sessions: List<TrainingSession>,
    val restDays: Int
)
