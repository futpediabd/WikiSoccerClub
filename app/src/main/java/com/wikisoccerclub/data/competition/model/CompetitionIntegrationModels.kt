package com.wikisoccerclub.data.competition.model

import com.wikisoccerclub.data.competition.engine.GeneratedCompetitionStructure
import com.wikisoccerclub.data.competition.engine.QualificationSource

data class CompetitionIntegrationRequest(
    val season: Int,
    val officialCompetitions: List<OfficialCompetitionCreationRequest>,
    val competitionStartDays: Map<String, Int>,
    val daysBetweenMatches: Int = 7
)

data class IntegratedCompetitionSeason(
    val competitionId: String,
    val competitionName: String,
    val season: Int,
    val participants: List<String>,
    val structure: GeneratedCompetitionStructure,
    val calendar: List<CompetitionCalendarSlot>,
    val warnings: List<String>
)

data class GlobalCompetitionIntegrationResult(
    val season: Int,
    val competitions: List<IntegratedCompetitionSeason>,
    val calendarConflicts: List<GlobalCalendarConflict>,
    val warnings: List<String>
)

data class GlobalCalendarConflict(
    val day: Int,
    val competitionIds: List<String>
)

data class SeasonAdvanceRequest(
    val season: Int,
    val leagueTables: Map<String, List<com.wikisoccerclub.data.competition.model.LeagueFinalPosition>>,
    val divisionLinks: Map<String, Pair<String?, String?>>,
    val promotionRelegationRules: Map<String, PromotionRelegationRule>,
    val qualificationSources: List<QualificationSource>
)
