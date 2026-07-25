package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionIntegrationValidationEngine
import com.wikisoccerclub.data.competition.engine.GlobalCompetitionCalendarEngine
import com.wikisoccerclub.data.competition.model.CompetitionIntegrationRequest
import com.wikisoccerclub.data.competition.model.GlobalCompetitionIntegrationResult
import com.wikisoccerclub.data.competition.model.IntegratedCompetitionSeason

class CompetitionIntegrationCoordinator(
    private val bootstrapService: OfficialCompetitionBootstrapService
) {

    fun integrate(
        request: CompetitionIntegrationRequest,
        normalizeCalendarConflicts: Boolean = true
    ): GlobalCompetitionIntegrationResult {
        val validation =
            CompetitionIntegrationValidationEngine.validate(request)

        require(validation.isValid) {
            validation.errors.joinToString(separator = "\n")
        }

        val bootstrapped = request.officialCompetitions.map { competition ->
            bootstrapService.bootstrap(
                request = competition,
                startDay = request.competitionStartDays[
                    competition.competitionId
                ] ?: 1,
                daysBetweenMatches = request.daysBetweenMatches
            )
        }

        val rawCalendars = bootstrapped.map { it.calendar }
        val calendars = if (normalizeCalendarConflicts) {
            GlobalCompetitionCalendarEngine.normalize(rawCalendars)
        } else {
            rawCalendars
        }

        val integrated = bootstrapped.mapIndexed { index, item ->
            IntegratedCompetitionSeason(
                competitionId =
                    item.seasonConfig.competitionId,
                competitionName =
                    request.officialCompetitions[index].competitionName,
                season = item.seasonConfig.season,
                participants =
                    item.seasonConfig.participantClubIds,
                structure = item.built.structure,
                calendar = calendars[index],
                warnings = item.built.creation.warnings
            )
        }

        val conflicts =
            GlobalCompetitionCalendarEngine.findConflicts(calendars)

        val warnings = buildList {
            addAll(validation.warnings)
            integrated.forEach { competition ->
                addAll(
                    competition.warnings.map {
                        "${competition.competitionId}: $it"
                    }
                )
            }
            if (conflicts.isNotEmpty()) {
                add("${conflicts.size} conflito(s) de calendário restante(s).")
            }
        }

        return GlobalCompetitionIntegrationResult(
            season = request.season,
            competitions = integrated,
            calendarConflicts = conflicts,
            warnings = warnings
        )
    }
}
