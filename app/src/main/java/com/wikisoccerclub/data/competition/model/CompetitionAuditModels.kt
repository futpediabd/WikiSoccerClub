package com.wikisoccerclub.data.competition.model

data class CompetitionAuditIssue(
    val competitionId: String,
    val severity: AuditSeverity,
    val code: String,
    val message: String
)

enum class AuditSeverity {
    INFO,
    WARNING,
    ERROR
}

data class CompetitionAuditSummary(
    val season: Int,
    val competitionCount: Int,
    val participantCount: Int,
    val scheduledMatchCount: Int,
    val conflictsFound: Int,
    val issues: List<CompetitionAuditIssue>
) {
    val isValid: Boolean
        get() = issues.none { it.severity == AuditSeverity.ERROR }
}

data class CompetitionSeasonSnapshot(
    val season: Int,
    val competitionIds: List<String>,
    val participantsByCompetition: Map<String, List<String>>,
    val matchDaysByCompetition: Map<String, List<Int>>,
    val warnings: List<String>
)
