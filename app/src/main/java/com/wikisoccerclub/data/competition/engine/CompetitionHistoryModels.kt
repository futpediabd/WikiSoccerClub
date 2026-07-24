package com.wikisoccerclub.data.competition.engine

data class ClubLeagueHistory(
    val clubId: String,
    val competitionId: String,
    val seasonsPlayed: Int = 0,
    val titles: Int = 0,
    val runnerUps: Int = 0,
    val promotions: Int = 0,
    val relegations: Int = 0,
    val matches: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0
)

data class LeagueTableMarker(
    val position: Int,
    val internationalQualification: String? = null,
    val promoted: Boolean = false,
    val relegated: Boolean = false
)
