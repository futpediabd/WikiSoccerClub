package com.wikisoccerclub.data.youth

enum class YouthPositionFilter {
    ALL,
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    ATTACKER
}

enum class YouthPlayerStatus {
    AVAILABLE,
    SIGNED,
    REJECTED
}

data class YouthTryoutConfig(
    val clubId: String,
    val clubCountry: String,
    val positionFilter: YouthPositionFilter = YouthPositionFilter.ALL,
    val minimumAge: Int = 15,
    val maximumAge: Int = 21,
    val candidateCount: Int = 6,
    val localNationalityChance: Int = 70,
    val cost: Long = 0L
)

data class YouthCandidate(
    val id: String,
    val name: String,
    val nationality: String,
    val age: Int,
    val position: YouthPositionFilter,
    val specificPosition: String,
    val overall: Int,
    val potential: Int,
    val salaryRequest: Long,
    val status: YouthPlayerStatus = YouthPlayerStatus.AVAILABLE
)

data class YouthTryoutResult(
    val id: String,
    val clubId: String,
    val positionFilter: YouthPositionFilter,
    val candidates: List<YouthCandidate>,
    val cost: Long,
    val generatedDay: Int
)
