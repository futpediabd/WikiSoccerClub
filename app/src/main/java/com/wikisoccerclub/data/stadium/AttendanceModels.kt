package com.wikisoccerclub.data.stadium

data class MatchAttendanceContext(
    val matchId: String,
    val seasonYear: Int,
    val homeClubId: String,
    val awayClubId: String,
    val competitionName: String,
    val stadium: Stadium,
    val homeClubReputation: Int,
    val awayClubReputation: Int,
    val homeLeaguePosition: Int?,
    val rivalryLevel: Int = 0,
    val isFinal: Boolean = false,
    val recentHomeForm: Int = 50,
    val weatherFactor: Double = 1.0
)

data class SectorAttendance(
    val sectorId: String,
    val attendance: Int,
    val ticketPrice: Long,
    val grossRevenue: Long
)

data class MatchAttendanceResult(
    val matchId: String,
    val totalAttendance: Int,
    val occupancyRate: Double,
    val grossTicketRevenue: Long,
    val maintenanceCost: Long,
    val netMatchRevenue: Long,
    val sectors: List<SectorAttendance>
)
