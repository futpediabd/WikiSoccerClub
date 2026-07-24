package com.wikisoccerclub.data.competition

import com.wikisoccerclub.data.match.CompletedMatchResult

object GroupStageEngine {

    fun applyResult(
        progress: GroupStageProgress,
        result: CompletedMatchResult,
        qualifiedPerGroup: Int
    ): GroupStageProgress {
        val matchIndex = progress.matches.indexOfFirst {
            it.id == result.matchId
        }

        if (matchIndex < 0) return progress

        val originalMatch = progress.matches[matchIndex]
        if (originalMatch.played) return progress

        val updatedMatches = progress.matches.toMutableList()
        updatedMatches[matchIndex] = originalMatch.copy(
            played = true,
            homeGoals = result.homeGoals,
            awayGoals = result.awayGoals
        )

        val updatedGroups = progress.groups.map { group ->
            if (group.id != originalMatch.groupId) {
                group
            } else {
                group.copy(
                    standings = StandingsEngine.applyResult(
                        standings = group.standings,
                        result = result,
                        teamNames = group.standings.entries.associate {
                            it.teamId to it.teamName
                        }
                    )
                )
            }
        }

        val roundFinished = updatedMatches
            .filter { it.round == progress.currentRound }
            .all { it.played }

        val lastRound = updatedMatches.maxOfOrNull { it.round } ?: 1
        val allPlayed = updatedMatches.all { it.played }

        val qualified = if (allPlayed) {
            updatedGroups.flatMap { group ->
                group.standings.entries
                    .take(qualifiedPerGroup)
                    .map { it.teamId }
            }
        } else {
            emptyList()
        }

        return progress.copy(
            groups = updatedGroups,
            matches = updatedMatches,
            currentRound = if (roundFinished && progress.currentRound < lastRound) {
                progress.currentRound + 1
            } else {
                progress.currentRound
            },
            qualifiedTeamIds = qualified,
            completed = allPlayed
        )
    }

    fun nextMatchForTeam(
        progress: GroupStageProgress,
        teamId: String
    ): GroupStageMatch? =
        progress.matches.firstOrNull {
            !it.played &&
                (it.homeTeamId == teamId ||
                    it.awayTeamId == teamId)
        }
}
