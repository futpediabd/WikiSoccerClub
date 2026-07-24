package com.wikisoccerclub.data.youth

enum class YouthPosition {
    GOALKEEPER,
    RIGHT_BACK,
    LEFT_BACK,
    CENTER_BACK,
    DEFENSIVE_MIDFIELDER,
    CENTRAL_MIDFIELDER,
    ATTACKING_MIDFIELDER,
    RIGHT_WINGER,
    LEFT_WINGER,
    STRIKER
}

enum class YouthProspectStatus {
    TRIAL,
    ACADEMY,
    PROMOTED,
    RELEASED
}

data class YouthAcademy(
    val clubId: String,
    val level: Int,
    val recruitmentLevel: Int,
    val coachingLevel: Int,
    val monthlyCost: Long,
    val prospectIds: List<String> = emptyList()
)

data class YouthProspect(
    val id: String,
    val name: String,
    val nationality: String,
    val age: Int,
    val position: YouthPosition,
    val overall: Int,
    val potential: Int,
    val preferredFoot: String,
    val characteristics: List<String>,
    val status: YouthProspectStatus =
        YouthProspectStatus.TRIAL
)

data class YouthIntakeRequest(
    val clubId: String,
    val seasonYear: Int,
    val academyLevel: Int,
    val recruitmentLevel: Int,
    val preferredNationalities: List<String>,
    val amount: Int = 8
)

data class YouthIntakeResult(
    val seasonYear: Int,
    val clubId: String,
    val prospects: List<YouthProspect>,
    val totalCost: Long
)
