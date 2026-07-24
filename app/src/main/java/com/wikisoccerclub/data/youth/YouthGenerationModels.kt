package com.wikisoccerclub.data.youth

data class YouthIntakeConfig(
    val clubId: String,
    val seasonYear: Int,
    val country: String,
    val region: String?,
    val minimumPlayers: Int = 4,
    val maximumPlayers: Int = 8,
    val minimumAge: Int = 15,
    val maximumAge: Int = 18,
    val foreignPlayerChance: Int = 8,
    val goldenGenerationChance: Int = 3
)

data class YouthNamePool(
    val nationality: String,
    val firstNames: List<String>,
    val lastNames: List<String>
)

data class YouthIntakeResult(
    val seasonYear: Int,
    val clubId: String,
    val players: List<YouthPlayer>,
    val goldenGeneration: Boolean,
    val averagePotential: Double,
    val bestPlayerId: String?
)

data class YouthDevelopmentProjection(
    val playerId: String,
    val projectedOverallAt18: Int,
    val projectedOverallAt21: Int,
    val developmentRisk: Int,
    val developmentLabel: String
)
