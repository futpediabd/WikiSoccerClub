package com.wikisoccerclub.data.match

data class PlayerMatchUpdate(
    val playerId: String,
    val finalEnergy: Int,
    val yellowCards: Int = 0,
    val redCards: Int = 0,
    val injured: Boolean = false,
    val injuryRounds: Int = 0
)

data class CompletedMatchResult(
    val matchId: String,
    val homeTeamId: String,
    val awayTeamId: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val homeShots: Int,
    val awayShots: Int,
    val homeShotsOnTarget: Int,
    val awayShotsOnTarget: Int,
    val events: List<LiveMatchEvent>,
    val substitutions: List<com.wikisoccerclub.data.competition.MatchSubstitution>,
    val playerUpdates: List<PlayerMatchUpdate>
)
