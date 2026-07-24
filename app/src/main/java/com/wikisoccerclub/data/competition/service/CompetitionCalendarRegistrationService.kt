package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionCalendarConflictEngine
import com.wikisoccerclub.data.competition.model.SeasonCalendarRegistration

class CompetitionCalendarRegistrationService {

    fun registerWithoutConflicts(
        existing: List<SeasonCalendarRegistration>,
        newRegistration: SeasonCalendarRegistration
    ): SeasonCalendarRegistration {
        val occupiedDays = existing
            .filter { it.season == newRegistration.season }
            .flatMap { it.matchDays }
            .toSet()

        return CompetitionCalendarConflictEngine.moveConflictingDays(
            registration = newRegistration,
            occupiedDays = occupiedDays
        )
    }
}
