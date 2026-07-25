package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.AuditSeverity
import com.wikisoccerclub.data.competition.model.CompetitionAuditIssue
import com.wikisoccerclub.data.competition.model.CompetitionAuditSummary
import com.wikisoccerclub.data.competition.model.GlobalCompetitionIntegrationResult

object CompetitionAuditEngine {

    fun audit(
        result: GlobalCompetitionIntegrationResult
    ): CompetitionAuditSummary {
        val issues = mutableListOf<CompetitionAuditIssue>()

        result.competitions.forEach { competition ->
            if (competition.participants.isEmpty()) {
                issues += CompetitionAuditIssue(
                    competitionId = competition.competitionId,
                    severity = AuditSeverity.ERROR,
                    code = "NO_PARTICIPANTS",
                    message = "A competição não possui participantes."
                )
            }

            val duplicateParticipants = competition.participants
                .groupingBy { it }
                .eachCount()
                .filterValues { it > 1 }
                .keys

            if (duplicateParticipants.isNotEmpty()) {
                issues += CompetitionAuditIssue(
                    competitionId = competition.competitionId,
                    severity = AuditSeverity.ERROR,
                    code = "DUPLICATE_PARTICIPANTS",
                    message = "Há clubes participantes duplicados."
                )
            }

            if (competition.calendar.isEmpty()) {
                issues += CompetitionAuditIssue(
                    competitionId = competition.competitionId,
                    severity = AuditSeverity.WARNING,
                    code = "EMPTY_CALENDAR",
                    message = "Nenhuma data foi gerada para a competição."
                )
            }

            val duplicateDays = competition.calendar
                .groupingBy { it.day }
                .eachCount()
                .filterValues { it > 1 }
                .keys

            if (duplicateDays.isNotEmpty()) {
                issues += CompetitionAuditIssue(
                    competitionId = competition.competitionId,
                    severity = AuditSeverity.WARNING,
                    code = "DUPLICATE_MATCH_DAYS",
                    message = "A competição possui datas internas repetidas."
                )
            }

            competition.warnings.forEach { warning ->
                issues += CompetitionAuditIssue(
                    competitionId = competition.competitionId,
                    severity = AuditSeverity.WARNING,
                    code = "COMPETITION_WARNING",
                    message = warning
                )
            }
        }

        result.calendarConflicts.forEach { conflict ->
            issues += CompetitionAuditIssue(
                competitionId = conflict.competitionIds.joinToString(),
                severity = AuditSeverity.ERROR,
                code = "GLOBAL_CALENDAR_CONFLICT",
                message = "Conflito global encontrado no dia ${conflict.day}."
            )
        }

        result.warnings.forEach { warning ->
            issues += CompetitionAuditIssue(
                competitionId = "GLOBAL",
                severity = AuditSeverity.INFO,
                code = "GLOBAL_WARNING",
                message = warning
            )
        }

        return CompetitionAuditSummary(
            season = result.season,
            competitionCount = result.competitions.size,
            participantCount = result.competitions
                .sumOf { it.participants.size },
            scheduledMatchCount = result.competitions
                .sumOf { it.calendar.size },
            conflictsFound = result.calendarConflicts.size,
            issues = issues
        )
    }
}
