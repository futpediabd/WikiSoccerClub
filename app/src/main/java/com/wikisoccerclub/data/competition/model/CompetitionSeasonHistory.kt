package com.wikisoccerclub.data.competition.model

data class CompetitionSeasonHistory(
    val competitionId: String,
    val competitionName: String,
    val season: Int,
    val championClubId: String,
    val runnerUpClubId: String,
    val thirdPlaceClubId: String? = null,
    val fourthPlaceClubId: String? = null,
    val participantClubIds: List<String>,
    val totalMatches: Int,
    val totalGoals: Int
)

data class ClubCompetitionHistory(
    val clubId: String,
    val competitionId: String,
    val participations: Int = 0,
    val titles: Int = 0,
    val runnerUps: Int = 0,
    val thirdPlaces: Int = 0,
    val fourthPlaces: Int = 0,
    val matches: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0
) {
    val goalDifference: Int
        get() = goalsFor - goalsAgainst
}
