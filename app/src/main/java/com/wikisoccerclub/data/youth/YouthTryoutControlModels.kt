package com.wikisoccerclub.data.youth

data class YouthTryoutControl(
    val clubId: String,
    val lastTryoutDay: Int? = null
)

data class YouthTryoutAvailability(
    val available: Boolean,
    val remainingDays: Int,
    val cost: Long,
    val message: String
)

data class YouthSignedPlayer(
    val id: String,
    val name: String,
    val nationality: String,
    val age: Int,
    val positionGroup: YouthPositionFilter,
    val positionName: String,
    val overall: Int,
    val potential: Int,
    val salary: Long,
    val contractYears: Int = 3
)
