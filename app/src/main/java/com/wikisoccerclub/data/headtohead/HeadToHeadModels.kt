package com.wikisoccerclub.data.headtohead

import com.wikisoccerclub.data.transfer.CareerDate

data class HeadToHeadMatch(
    val matchId: String,
    val date: CareerDate,
    val competitionId: String,
    val competitionName: String,
    val homeClubId: String,
    val homeClubName: String,
    val awayClubId: String,
    val awayClubName: String,
    val homeGoals: Int,
    val awayGoals: Int
)

data class HeadToHeadScoreRecord(
    val matchId: String,
    val date: CareerDate,
    val competitionName: String,
    val homeClubName: String,
    val awayClubName: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val goalDifference: Int
)

data class ClubVenueRecord(
    val biggestHomeWin: HeadToHeadScoreRecord?,
    val biggestAwayWin: HeadToHeadScoreRecord?,
    val biggestHomeDefeat: HeadToHeadScoreRecord?,
    val biggestAwayDefeat: HeadToHeadScoreRecord?
)

data class ClubHeadToHeadSummary(
    val clubId: String,
    val clubName: String,
    val opponentId: String,
    val opponentName: String,
    val matches: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val homeMatches: Int,
    val awayMatches: Int,
    val homeWins: Int,
    val awayWins: Int,
    val records: ClubVenueRecord,
    val recentMatches: List<HeadToHeadMatch>
)

data class HeadToHeadSummary(
    val firstClub: ClubHeadToHeadSummary,
    val secondClub: ClubHeadToHeadSummary,
    val competitionFilter: String?,
    val allMatches: List<HeadToHeadMatch>
)
