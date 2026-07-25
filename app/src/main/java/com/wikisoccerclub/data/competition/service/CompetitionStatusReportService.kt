package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.model.AuditSeverity
import com.wikisoccerclub.data.competition.model.CompetitionAuditSummary

object CompetitionStatusReportService {

    fun buildTextReport(
        audit: CompetitionAuditSummary
    ): String {
        val errors = audit.issues.count {
            it.severity == AuditSeverity.ERROR
        }
        val warnings = audit.issues.count {
            it.severity == AuditSeverity.WARNING
        }
        val information = audit.issues.count {
            it.severity == AuditSeverity.INFO
        }

        return buildString {
            appendLine("Temporada: ${audit.season}")
            appendLine("Competições: ${audit.competitionCount}")
            appendLine("Participantes: ${audit.participantCount}")
            appendLine("Datas programadas: ${audit.scheduledMatchCount}")
            appendLine("Conflitos: ${audit.conflictsFound}")
            appendLine("Erros: $errors")
            appendLine("Avisos: $warnings")
            appendLine("Informações: $information")
            appendLine("Situação: ${if (audit.isValid) "VÁLIDA" else "COM ERROS"}")

            if (audit.issues.isNotEmpty()) {
                appendLine()
                appendLine("Ocorrências:")
                audit.issues.forEach { issue ->
                    appendLine(
                        "[${issue.severity}] ${issue.competitionId} - " +
                            "${issue.code}: ${issue.message}"
                    )
                }
            }
        }
    }
}
