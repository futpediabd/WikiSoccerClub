package com.wikisoccerclub.data.competition

import com.wikisoccerclub.data.match.CompletedMatchResult

object CompetitionProgressEngine {

    fun applyMatchResult(
        progress: CompetitionProgress,
        result: CompletedMatchResult,
        teamNames: Map<String, String>
    ): CompetitionProgress {
        val matchIndex = progress.matches.indexOfFirst {
            it.id == result.matchId
        }

        if (matchIndex < 0) return progress

        val updatedMatches = progress.matches.toMutableList()
        updatedMatches[matchIndex] = updatedMatches[matchIndex].copy(
            played = true,
            homeGoals = result.homeGoals,
            awayGoals = result.awayGoals
        )

        val updatedStandings = StandingsEngine.applyResult(
            standings = progress.standings,
            result = result,
            teamNames = teamNames
        )

        val roundFinished = updatedMatches
            .filter { it.round == progress.currentRound }
            .all { it.played }

        return progress.copy(
            currentRound = if (roundFinished) {
                progress.currentRound + 1
            } else {
                progress.currentRound
            },
            matches = updatedMatches,
            standings = updatedStandings
        )
    }

    fun nextMatchForTeam(
        progress: CompetitionProgress,
        teamId: String
    ): CompetitionMatch? =
        progress.matches
            .filter { !it.played }
            .sortedWith(
                compareBy<CompetitionMatch> { it.round }
                    .thenBy { it.id }
            )
            .firstOrNull {
                it.homeTeamId == teamId ||
                    it.awayTeamId == teamId
            }
}
