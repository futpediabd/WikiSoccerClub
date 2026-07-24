package com.wikisoccerclub.data.ban

data class BanClub(
    val sourceFile: String,
    val name: String,
    val country: String,
    val city: String,
    val stadiumName: String,
    val stadiumCapacity: Int,
    val level: Int,
    val strength: Int,
    val stars: Double,
    val players: List<BanPlayer>
)

data class BanPlayer(
    val name: String,
    val age: Int,
    val nationality: String,
    val position: String,
    val overall: Int,
    val potential: Int
)
