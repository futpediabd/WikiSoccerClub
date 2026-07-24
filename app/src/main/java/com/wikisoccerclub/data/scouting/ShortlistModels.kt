package com.wikisoccerclub.data.scouting

enum class ShortlistPriority {
    MONITOR,
    INTERESTED,
    PRIORITY,
    NEGOTIATE
}

data class ShortlistEntry(
    val playerId: String,
    val addedSeasonYear: Int,
    val priority: ShortlistPriority,
    val note: String = "",
    val lastReportId: String? = null
)
