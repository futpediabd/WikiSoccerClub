package com.wikisoccerclub.data.competition

enum class CompetitionZoneType {
    CHAMPION,
    PROMOTION,
    CONTINENTAL,
    PLAYOFF,
    RELEGATION,
    NONE
}

data class CompetitionZoneRule(
    val startPosition: Int,
    val endPosition: Int,
    val type: CompetitionZoneType,
    val label: String
)

data class StandingWithZone(
    val position: Int,
    val entry: StandingEntry,
    val zone: CompetitionZoneRule?
)
