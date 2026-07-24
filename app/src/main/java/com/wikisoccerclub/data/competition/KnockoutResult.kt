package com.wikisoccerclub.data.competition

data class KnockoutMatchResult(
    val matchId: String,
    val homeTeamId: String,
    val awayTeamId: String,
    val homeGoals: Int,
    val awayGoals: Int,
    val penaltiesHome: Int? = null,
    val penaltiesAway: Int? = null
)
