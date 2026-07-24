package com.wikisoccerclub.data.scouting

enum class ScoutingRegion {
    BRAZIL,
    SOUTH_AMERICA,
    NORTH_AMERICA,
    CENTRAL_AMERICA,
    EUROPE,
    AFRICA,
    ASIA,
    OCEANIA,
    WORLDWIDE
}

enum class ScoutingStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class PlayerPositionGroup {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    ATTACKER,
    ANY
}

data class ScoutProfile(
    val id: String,
    val name: String,
    val nationality: String,
    val ability: Int,
    val potentialJudgement: Int,
    val adaptability: Int,
    val regionKnowledge: Map<ScoutingRegion, Int>,
    val monthlySalary: Long
)

data class ScoutingAssignment(
    val id: String,
    val scoutId: String,
    val region: ScoutingRegion,
    val positionGroup: PlayerPositionGroup,
    val minimumAge: Int,
    val maximumAge: Int,
    val minimumPotential: Int,
    val durationDays: Int,
    val startDay: Int,
    val status: ScoutingStatus = ScoutingStatus.PLANNED
)

data class ScoutedPlayer(
    val playerId: String,
    val name: String,
    val age: Int,
    val nationality: String,
    val clubId: String?,
    val positionGroup: PlayerPositionGroup,
    val currentAbility: Int,
    val potential: Int,
    val marketValue: Long,
    val wageDemand: Long,
    val contractMonthsRemaining: Int,
    val interestedInMove: Boolean,
    val hiddenCurrentAbility: Int? = null,
    val hiddenPotential: Int? = null
)

data class ScoutingReport(
    val id: String,
    val assignmentId: String,
    val scoutId: String,
    val playerId: String,
    val observedCurrentAbility: Int,
    val observedPotential: Int,
    val confidence: Int,
    val estimatedMarketValue: Long,
    val estimatedWageDemand: Long,
    val recommendationScore: Int,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val generatedDay: Int
)
