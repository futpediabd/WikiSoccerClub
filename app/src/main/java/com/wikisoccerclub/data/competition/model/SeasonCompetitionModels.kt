package com.wikisoccerclub.data.competition.model

enum class QualificationMarkerType {
    CONTINENTAL_PRIMARY,
    CONTINENTAL_SECONDARY,
    INTERNATIONAL,
    PROMOTION,
    PLAYOFF,
    RELEGATION
}

data class QualificationMarker(
    val fromPosition: Int,
    val toPosition: Int = fromPosition,
    val type: QualificationMarkerType,
    val label: String,
    val targetCompetitionId: String? = null
)

data class LeagueStandingEntry(
    val clubId: String,
    val clubName: String,
    val position: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val points: Int,
    val markers: List<QualificationMarker> = emptyList()
) {
    val goalDifference: Int get() = goalsFor - goalsAgainst
}

data class SeasonCompetitionConfig(
    val season: Int,
    val competitionId: String,
    val participantClubIds: List<String>,
    val markers: List<QualificationMarker> = emptyList()
)

data class CompetitionCalendarSlot(
    val competitionId: String,
    val season: Int,
    val roundName: String,
    val day: Int,
    val leg: Int = 1
)
