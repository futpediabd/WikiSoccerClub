package com.wikisoccerclub.data.records

enum class RecordCategory {
    MOST_MATCHES,
    MOST_WINS,
    MOST_GOALS,
    MOST_ASSISTS,
    MOST_CLEAN_SHEETS,
    MOST_TITLES,
    BIGGEST_WIN,
    LONGEST_WINNING_STREAK,
    LONGEST_UNBEATEN_STREAK,
    HIGHEST_ATTENDANCE,
    HIGHEST_TRANSFER_FEE
}

enum class RecordScope {
    CLUB,
    COMPETITION,
    COUNTRY,
    CONTINENT,
    WORLD
}

data class FootballRecord(
    val id: String,
    val category: RecordCategory,
    val scope: RecordScope,
    val scopeId: String?,
    val holderId: String,
    val holderName: String,
    val value: Long,
    val secondaryValue: Long = 0,
    val seasonYear: Int?,
    val description: String
)

data class PlayerCareerStats(
    val playerId: String,
    val playerName: String,
    val matches: Int = 0,
    val starts: Int = 0,
    val goals: Int = 0,
    val assists: Int = 0,
    val cleanSheets: Int = 0,
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val titles: Int = 0
)

data class ClubCareerStats(
    val clubId: String,
    val clubName: String,
    val matches: Int = 0,
    val wins: Int = 0,
    val draws: Int = 0,
    val losses: Int = 0,
    val goalsFor: Int = 0,
    val goalsAgainst: Int = 0,
    val titles: Int = 0,
    val highestAttendance: Int = 0,
    val biggestTransferFee: Long = 0
)

data class MatchRecordInput(
    val matchId: String,
    val competitionId: String,
    val seasonYear: Int,
    val homeClubId: String,
    val homeClubName: String,
    val awayClubId: String,
    val awayClubName: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val attendance: Int = 0
)
