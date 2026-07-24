package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionCalendarSlot

object CompetitionCalendarEngine {

    fun createSchedule(
        competitionId: String,
        season: Int,
        startDay: Int,
        structure: GeneratedCompetitionStructure,
        daysBetweenMatches: Int = 7
    ): List<CompetitionCalendarSlot> {
        val slots = mutableListOf<CompetitionCalendarSlot>()
        var currentDay = startDay

        if (structure.groups > 0) {
            repeat(6) { index ->
                slots += CompetitionCalendarSlot(
                    competitionId,
                    season,
                    "Fase de grupos - Rodada ${index + 1}",
                    currentDay
                )
                currentDay += daysBetweenMatches
            }
        }

        structure.knockoutRounds.forEach { round ->
            slots += CompetitionCalendarSlot(
                competitionId, season, "$round - Ida", currentDay, 1
            )
            currentDay += daysBetweenMatches
            slots += CompetitionCalendarSlot(
                competitionId, season, "$round - Volta", currentDay, 2
            )
            currentDay += daysBetweenMatches
        }

        return slots
    }
}
