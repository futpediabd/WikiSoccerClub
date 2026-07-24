package com.wikisoccerclub.data.season

data class CareerPlayer(
    val id: String,
    val name: String,
    val age: Int,
    val overall: Int,
    val potential: Int,
    val contractUntilYear: Int,
    val retired: Boolean = false
)

data class PlayerSeasonUpdate(
    val playerId: String,
    val previousAge: Int,
    val newAge: Int,
    val previousOverall: Int,
    val newOverall: Int,
    val contractExpired: Boolean,
    val retired: Boolean
)
