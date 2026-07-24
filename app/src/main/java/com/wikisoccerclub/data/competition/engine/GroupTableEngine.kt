package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionMatchResult
import com.wikisoccerclub.data.competition.model.GroupQualificationResult
import com.wikisoccerclub.data.competition.model.GroupStandingRow

object GroupTableEngine {

    fun buildTable(
        groupName: String,
        clubIds: List<String>,
        matches: List<CompetitionMatchResult>,
        qualifiedCount: Int = 2,
        disciplinaryPoints: Map<String, Int> = emptyMap()
    ): GroupQualificationResult {
        require(qualifiedCount in 1 until clubIds.size)

        val table = clubIds.associateWith {
            GroupStandingRow(
                clubId = it,
                disciplinaryPoints = disciplinaryPoints[it] ?: 0
            )
        }.toMutableMap()

        matches
            .filter { it.groupName == groupName }
            .forEach { match ->
                val home = table.getValue(match.homeClubId)
                val away = table.getValue(match.awayClubId)

                val homeWin = match.homeGoals > match.awayGoals
                val awayWin = match.awayGoals > match.homeGoals
                val draw = match.homeGoals == match.awayGoals

                table[match.homeClubId] = home.copy(
                    played = home.played + 1,
                    wins = home.wins + if (homeWin) 1 else 0,
                    draws = home.draws + if (draw) 1 else 0,
                    losses = home.losses + if (awayWin) 1 else 0,
                    goalsFor = home.goalsFor + match.homeGoals,
                    goalsAgainst = home.goalsAgainst + match.awayGoals,
                    points = home.points + when {
                        homeWin -> 3
                        draw -> 1
                        else -> 0
                    }
                )

                table[match.awayClubId] = away.copy(
                    played = away.played + 1,
                    wins = away.wins + if (awayWin) 1 else 0,
                    draws = away.draws + if (draw) 1 else 0,
                    losses = away.losses + if (homeWin) 1 else 0,
                    goalsFor = away.goalsFor + match.awayGoals,
                    goalsAgainst = away.goalsAgainst + match.homeGoals,
                    points = away.points + when {
                        awayWin -> 3
                        draw -> 1
                        else -> 0
                    }
                )
            }

        val ordered = table.values.sortedWith(
            compareByDescending<GroupStandingRow> { it.points }
                .thenByDescending { it.wins }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
                .thenBy { it.disciplinaryPoints }
                .thenBy { it.clubId }
        )

        return GroupQualificationResult(
            groupName = groupName,
            table = ordered,
            qualifiedClubIds = ordered.take(qualifiedCount).map { it.clubId }
        )
    }
}
