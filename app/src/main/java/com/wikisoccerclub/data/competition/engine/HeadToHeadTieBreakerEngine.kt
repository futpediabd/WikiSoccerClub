package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionMatchResult
import com.wikisoccerclub.data.competition.model.GroupStandingRow

object HeadToHeadTieBreakerEngine {

    fun resolve(
        tiedRows: List<GroupStandingRow>,
        matches: List<CompetitionMatchResult>
    ): List<GroupStandingRow> {
        if (tiedRows.size < 2) return tiedRows

        val tiedIds = tiedRows.map { it.clubId }.toSet()

        val points = tiedIds.associateWith { 0 }.toMutableMap()
        val goalDifference = tiedIds.associateWith { 0 }.toMutableMap()
        val goalsFor = tiedIds.associateWith { 0 }.toMutableMap()

        matches
            .filter {
                it.homeClubId in tiedIds && it.awayClubId in tiedIds
            }
            .forEach { match ->
                when {
                    match.homeGoals > match.awayGoals -> {
                        points[match.homeClubId] = points.getValue(match.homeClubId) + 3
                    }
                    match.awayGoals > match.homeGoals -> {
                        points[match.awayClubId] = points.getValue(match.awayClubId) + 3
                    }
                    else -> {
                        points[match.homeClubId] = points.getValue(match.homeClubId) + 1
                        points[match.awayClubId] = points.getValue(match.awayClubId) + 1
                    }
                }

                goalDifference[match.homeClubId] =
                    goalDifference.getValue(match.homeClubId) +
                        (match.homeGoals - match.awayGoals)

                goalDifference[match.awayClubId] =
                    goalDifference.getValue(match.awayClubId) +
                        (match.awayGoals - match.homeGoals)

                goalsFor[match.homeClubId] =
                    goalsFor.getValue(match.homeClubId) + match.homeGoals

                goalsFor[match.awayClubId] =
                    goalsFor.getValue(match.awayClubId) + match.awayGoals
            }

        return tiedRows.sortedWith(
            compareByDescending<GroupStandingRow> {
                points.getValue(it.clubId)
            }.thenByDescending {
                goalDifference.getValue(it.clubId)
            }.thenByDescending {
                goalsFor.getValue(it.clubId)
            }.thenBy {
                it.clubId
            }
        )
    }
}
