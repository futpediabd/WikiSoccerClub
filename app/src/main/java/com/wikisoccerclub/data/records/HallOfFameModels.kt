package com.wikisoccerclub.data.records

enum class HallOfFameType {
    PLAYER,
    MANAGER,
    CLUB
}

data class HallOfFameEntry(
    val id: String,
    val type: HallOfFameType,
    val subjectId: String,
    val subjectName: String,
    val inductionYear: Int,
    val score: Int,
    val titles: Int,
    val appearances: Int = 0,
    val goals: Int = 0,
    val description: String
)

data class SeasonAward(
    val id: String,
    val seasonYear: Int,
    val competitionId: String?,
    val awardName: String,
    val winnerId: String,
    val winnerName: String,
    val value: Int = 0
)
