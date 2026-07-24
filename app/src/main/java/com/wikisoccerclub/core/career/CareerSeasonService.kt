package com.wikisoccerclub.core.career

import com.wikisoccerclub.data.career.CareerGlobalEvent
import com.wikisoccerclub.data.career.CareerGlobalEventType
import com.wikisoccerclub.data.career.CareerSeasonSummary
import com.wikisoccerclub.data.career.CompetitionSeasonOutcome
import com.wikisoccerclub.data.career.DivisionChangeType
import com.wikisoccerclub.data.career.DivisionMembershipChange
import com.wikisoccerclub.data.career.PromotionRelegationRule
import com.wikisoccerclub.data.career.CareerScheduleRepository
import com.wikisoccerclub.data.career.CareerTimelineRepository
import com.wikisoccerclub.data.competition.CareerStandingsRepository

/** Finaliza a temporada e calcula campeões, acessos e rebaixamentos. */
class CareerSeasonService(
    private val schedule: CareerScheduleRepository,
    private val standings: CareerStandingsRepository,
    private val timeline: CareerTimelineRepository
) {
    private val history = mutableListOf<CareerSeasonSummary>()

    fun canFinishSeason(): Boolean = schedule.all().isNotEmpty() && schedule.all().all {
        it.homeGoals != null && it.awayGoals != null
    }

    fun finishSeason(
        seasonYear: Int,
        rules: List<PromotionRelegationRule>
    ): CareerSeasonSummary {
        require(canFinishSeason()) { "Ainda existem partidas pendentes na temporada." }

        val outcomes = rules.map { rule ->
            val table = standings.getOrCreate(rule.competitionId, emptyMap())
            val promoted = table.entries.take(rule.promotionPlaces).map { it.teamId }
            val relegated = if (rule.relegationPlaces > 0) {
                table.entries.takeLast(rule.relegationPlaces).map { it.teamId }
            } else emptyList()
            val champion = table.entries.firstOrNull()
            CompetitionSeasonOutcome(
                competitionId = rule.competitionId,
                championClubId = champion?.teamId,
                championClubName = champion?.teamName,
                promotedClubIds = promoted,
                relegatedClubIds = relegated,
                finalStandings = table
            )
        }

        val summary = CareerSeasonSummary(
            seasonYear = seasonYear,
            finishedAt = timeline.currentDate(),
            outcomes = outcomes,
            champions = outcomes.mapNotNull { outcome ->
                outcome.championClubName?.let { outcome.competitionId to it }
            }.toMap(),
            promotedClubIds = outcomes.flatMap { it.promotedClubIds }.distinct(),
            relegatedClubIds = outcomes.flatMap { it.relegatedClubIds }.distinct()
        )
        history += summary
        timeline.saveEvents(listOf(
            CareerGlobalEvent(
                id = "season-ended-$seasonYear",
                date = summary.finishedAt,
                type = CareerGlobalEventType.SEASON_ENDED,
                title = "Temporada $seasonYear encerrada",
                message = "Campeões, acessos e rebaixamentos foram confirmados.",
                important = true
            )
        ))
        return summary
    }

    fun membershipChanges(
        summary: CareerSeasonSummary,
        rules: List<PromotionRelegationRule>
    ): List<DivisionMembershipChange> = rules.flatMap { rule ->
        val outcome = summary.outcomes.firstOrNull { it.competitionId == rule.competitionId }
            ?: return@flatMap emptyList()
        buildList {
            rule.nextDivisionId?.let { target ->
                outcome.promotedClubIds.forEach { clubId ->
                    add(DivisionMembershipChange(clubId, rule.competitionId, target, DivisionChangeType.PROMOTION))
                }
            }
            rule.previousDivisionId?.let { target ->
                outcome.relegatedClubIds.forEach { clubId ->
                    add(DivisionMembershipChange(clubId, rule.competitionId, target, DivisionChangeType.RELEGATION))
                }
            }
        }
    }

    fun history(): List<CareerSeasonSummary> = history.toList()
}
