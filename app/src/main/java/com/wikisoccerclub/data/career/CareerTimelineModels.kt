package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.transfer.CareerDate

enum class CareerGlobalEventType {
    SEASON_STARTED,
    TRANSFER_WINDOW_OPENED,
    TRANSFER_WINDOW_CLOSED,
    MATCH_DAY,
    SEASON_ENDED,
    INFORMATION
}

data class CareerGlobalEvent(
    val id: String,
    val date: CareerDate,
    val type: CareerGlobalEventType,
    val title: String,
    val message: String,
    val important: Boolean = false
)

data class CareerDayState(
    val currentDate: CareerDate,
    val seasonYear: Int,
    val dayNumber: Int,
    val transferWindowOpen: Boolean,
    val generatedEvents: List<CareerGlobalEvent>
)

data class CareerAdvanceResult(
    val previousDate: CareerDate,
    val currentState: CareerDayState,
    val allNewEvents: List<CareerGlobalEvent>
)
