package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.SeasonCalendarRegistration

data class CalendarConflict(
    val competitionId: String,
    val conflictingCompetitionId: String,
    val day: Int
)

object CompetitionCalendarConflictEngine {

    fun findConflicts(
        registrations: List<SeasonCalendarRegistration>
    ): List<CalendarConflict> {
        val conflicts = mutableListOf<CalendarConflict>()

        registrations.forEachIndexed { index, first ->
            registrations.drop(index + 1).forEach { second ->
                if (first.season != second.season) return@forEach

                val repeatedDays =
                    first.matchDays.toSet().intersect(second.matchDays.toSet())

                repeatedDays.forEach { day ->
                    conflicts += CalendarConflict(
                        competitionId = first.competitionId,
                        conflictingCompetitionId = second.competitionId,
                        day = day
                    )
                }
            }
        }

        return conflicts
    }

    fun moveConflictingDays(
        registration: SeasonCalendarRegistration,
        occupiedDays: Set<Int>,
        maximumDelay: Int = 30
    ): SeasonCalendarRegistration {
        val adjusted = mutableListOf<Int>()
        val localOccupied = occupiedDays.toMutableSet()

        registration.matchDays.sorted().forEach { originalDay ->
            var selectedDay = originalDay
            var delay = 0

            while (selectedDay in localOccupied && delay < maximumDelay) {
                selectedDay++
                delay++
            }

            require(selectedDay !in localOccupied) {
                "Não foi possível resolver o conflito de calendário."
            }

            adjusted += selectedDay
            localOccupied += selectedDay
        }

        return registration.copy(matchDays = adjusted)
    }
}
