package com.wikisoccerclub.core.career

import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.data.career.CareerTimelineRepository

/** Instâncias compartilhadas do calendário central da carreira. */
object CareerModule {
    val timeline: CareerTimelineRepository by lazy { CareerTimelineRepository() }

    val progression: CareerProgressionService by lazy {
        CareerProgressionService(
            timeline = timeline,
            windows = TransferModule.windows
        )
    }
}
