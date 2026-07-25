package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionAuditEngine
import com.wikisoccerclub.data.competition.engine.CompetitionSeasonSnapshotEngine
import com.wikisoccerclub.data.competition.model.CompetitionAuditSummary
import com.wikisoccerclub.data.competition.model.CompetitionIntegrationRequest
import com.wikisoccerclub.data.competition.model.CompetitionSeasonSnapshot
import com.wikisoccerclub.data.competition.model.GlobalCompetitionIntegrationResult

data class AuditedCompetitionSeason(
    val integration: GlobalCompetitionIntegrationResult,
    val audit: CompetitionAuditSummary,
    val snapshot: CompetitionSeasonSnapshot
)

class CompetitionSeasonAuditService(
    private val integrationCoordinator: CompetitionIntegrationCoordinator
) {

    fun integrateAndAudit(
        request: CompetitionIntegrationRequest,
        normalizeCalendarConflicts: Boolean = true,
        failOnAuditError: Boolean = true
    ): AuditedCompetitionSeason {
        val integration = integrationCoordinator.integrate(
            request = request,
            normalizeCalendarConflicts = normalizeCalendarConflicts
        )

        val audit = CompetitionAuditEngine.audit(integration)
        val snapshot = CompetitionSeasonSnapshotEngine.create(integration)

        if (failOnAuditError) {
            require(audit.isValid) {
                audit.issues
                    .filter { it.severity.name == "ERROR" }
                    .joinToString(separator = "\n") { issue ->
                        "${issue.code}: ${issue.message}"
                    }
            }
        }

        return AuditedCompetitionSeason(
            integration = integration,
            audit = audit,
            snapshot = snapshot
        )
    }
}
