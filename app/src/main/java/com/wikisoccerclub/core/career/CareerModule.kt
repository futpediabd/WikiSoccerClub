package com.wikisoccerclub.core.career

import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.data.career.CareerTimelineRepository
import com.wikisoccerclub.data.career.CareerScheduleRepository

/** Instâncias compartilhadas do calendário central da carreira. */
object CareerModule {
    val timeline: CareerTimelineRepository by lazy { CareerTimelineRepository() }
    val schedule: CareerScheduleRepository by lazy { CareerScheduleRepository() }

    val progression: CareerProgressionService by lazy {
        CareerProgressionService(
            timeline = timeline,
            windows = TransferModule.windows
        )
    }

    val matchDays: CareerMatchDayService by lazy {
        CareerMatchDayService(schedule = schedule, progression = progression)
    }
}
