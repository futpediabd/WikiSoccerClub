package com.wikisoccerclub.data.competition

import com.wikisoccerclub.data.match.CompletedMatchResult

object StandingsEngine {

    fun applyResult(
        standings: CompetitionStandings,
        result: CompletedMatchResult,
        teamNames: Map<String, String>
    ): CompetitionStandings {
        val entriesByTeam = standings.entries.associateBy {
            it.teamId
        }.toMutableMap()

        val home = entriesByTeam[result.homeTeamId]
            ?: StandingEntry(
                teamId = result.homeTeamId,
                teamName = teamNames[result.homeTeamId]
                    ?: result.homeTeamId
            )

        val away = entriesByTeam[result.awayTeamId]
            ?: StandingEntry(
                teamId = result.awayTeamId,
                teamName = teamNames[result.awayTeamId]
                    ?: result.awayTeamId
            )

        val homeWon = result.homeGoals > result.awayGoals
        val awayWon = result.awayGoals > result.homeGoals
        val draw = result.homeGoals == result.awayGoals

        entriesByTeam[result.homeTeamId] = home.copy(
            played = home.played + 1,
            wins = home.wins + if (homeWon) 1 else 0,
            draws = home.draws + if (draw) 1 else 0,
            losses = home.losses + if (awayWon) 1 else 0,
            goalsFor = home.goalsFor + result.homeGoals,
            goalsAgainst = home.goalsAgainst + result.awayGoals,
            points = home.points + when {
                homeWon -> 3
                draw -> 1
                else -> 0
            }
        )

        entriesByTeam[result.awayTeamId] = away.copy(
            played = away.played + 1,
            wins = away.wins + if (awayWon) 1 else 0,
            draws = away.draws + if (draw) 1 else 0,
            losses = away.losses + if (homeWon) 1 else 0,
            goalsFor = away.goalsFor + result.awayGoals,
            goalsAgainst = away.goalsAgainst + result.homeGoals,
            points = away.points + when {
                awayWon -> 3
                draw -> 1
                else -> 0
            }
        )

        return standings.copy(
            entries = sort(entriesByTeam.values.toList())
        )
    }

    fun sort(entries: List<StandingEntry>): List<StandingEntry> =
        entries.sortedWith(
            compareByDescending<StandingEntry> { it.points }
                .thenByDescending { it.wins }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
                .thenBy { it.teamName }
        )
}
