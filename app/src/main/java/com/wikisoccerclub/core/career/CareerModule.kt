package com.wikisoccerclub.core.career

import com.wikisoccerclub.core.transfer.TransferModule
import com.wikisoccerclub.data.career.CareerTimelineRepository
import com.wikisoccerclub.data.career.CareerScheduleRepository
import com.wikisoccerclub.data.competition.CareerStandingsRepository

/** Instâncias compartilhadas do calendário central da carreira. */
object CareerModule {
    val timeline: CareerTimelineRepository by lazy { CareerTimelineRepository() }
    val schedule: CareerScheduleRepository by lazy { CareerScheduleRepository() }
    val standings: CareerStandingsRepository by lazy { CareerStandingsRepository() }

    val competitions: CareerCompetitionService by lazy {
        CareerCompetitionService(schedule = schedule, standingsRepository = standings)
    }

    val progression: CareerProgressionService by lazy {
        CareerProgressionService(
            timeline = timeline,
            windows = TransferModule.windows
        )
    }

    val seasons: CareerSeasonService by lazy {
        CareerSeasonService(schedule = schedule, standings = standings, timeline = timeline)
    }

    val matchDays: CareerMatchDayService by lazy {
        CareerMatchDayService(
            schedule = schedule,
            progression = progression,
            competitions = competitions
        )
    }
}
