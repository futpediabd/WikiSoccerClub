package com.wikisoccerclub.data.match

data class LiveMatchState(
    val currentMinute: Int = 0,
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val homeShots: Int = 0,
    val awayShots: Int = 0,
    val homeShotsOnTarget: Int = 0,
    val awayShotsOnTarget: Int = 0,
    val speed: MatchSpeed = MatchSpeed.NORMAL,
    val paused: Boolean = false,
    val pausedForSubstitution: Boolean = false,
    val halfTimeCompleted: Boolean = false,
    val finished: Boolean = false,
    val events: List<LiveMatchEvent> = emptyList()
)
