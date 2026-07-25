package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionCalendarSlot
import com.wikisoccerclub.data.competition.model.GlobalCalendarConflict

object GlobalCompetitionCalendarEngine {

    fun findConflicts(
        calendars: List<List<CompetitionCalendarSlot>>
    ): List<GlobalCalendarConflict> {
        return calendars
            .flatten()
            .groupBy { it.day }
            .mapNotNull { (day, slots) ->
                val competitionIds = slots
                    .map { it.competitionId }
                    .distinct()

                if (competitionIds.size > 1) {
                    GlobalCalendarConflict(
                        day = day,
                        competitionIds = competitionIds
                    )
                } else {
                    null
                }
            }
            .sortedBy { it.day }
    }

    fun normalize(
        calendars: List<List<CompetitionCalendarSlot>>
    ): List<List<CompetitionCalendarSlot>> {
        val occupied = mutableSetOf<Int>()

        return calendars.map { calendar ->
            calendar
                .sortedBy { it.day }
                .map { slot ->
                    var day = slot.day
                    while (day in occupied) {
                        day++
                    }
                    occupied += day
                    slot.copy(day = day)
                }
        }
    }
}
