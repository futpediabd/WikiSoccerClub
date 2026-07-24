package com.wikisoccerclub.core.records

import com.wikisoccerclub.data.headtohead.HeadToHeadMatch
import com.wikisoccerclub.data.headtohead.HeadToHeadRepository
import com.wikisoccerclub.data.records.*

/** Calcula sequências atuais e recordes históricos a partir das partidas concluídas. */
class ClubStreakService(
    private val repository: HeadToHeadRepository
) {
    fun summary(clubId: String, competitionId: String? = null): ClubStreakSummary? {
        val matches = repository.all()
            .asSequence()
            .filter { it.homeClubId == clubId || it.awayClubId == clubId }
            .filter { competitionId == null || it.competitionId == competitionId }
            .sortedWith(compareBy<HeadToHeadMatch> { it.date }.thenBy { it.matchId })
            .toList()
        if (matches.isEmpty()) return null

        val clubName = if (matches.last().homeClubId == clubId) matches.last().homeClubName else matches.last().awayClubName
        val current = StreakType.entries.associateWith { type -> currentStreak(clubId, clubName, type, matches, competitionId) }
        val records = StreakType.entries.associateWith { type -> recordStreak(clubId, clubName, type, matches, competitionId) }

        return ClubStreakSummary(
            clubId = clubId,
            clubName = clubName,
            current = current,
            records = records,
            matchesAnalyzed = matches.size,
            competitionFilter = competitionId
        )
    }

    private fun currentStreak(
        clubId: String,
        clubName: String,
        type: StreakType,
        matches: List<HeadToHeadMatch>,
        competitionId: String?
    ): ClubStreak? {
        val selected = matches.asReversed().takeWhile { qualifies(clubId, it, type) }.reversed()
        if (selected.isEmpty()) return null
        return ClubStreak(clubId, clubName, type, selected.size, selected.first(), selected.last(), true, competitionId)
    }

    private fun recordStreak(
        clubId: String,
        clubName: String,
        type: StreakType,
        matches: List<HeadToHeadMatch>,
        competitionId: String?
    ): ClubStreak? {
        var best: List<HeadToHeadMatch> = emptyList()
        var run = mutableListOf<HeadToHeadMatch>()
        for (match in matches) {
            if (qualifies(clubId, match, type)) {
                run.add(match)
                if (run.size > best.size) best = run.toList()
            } else {
                run = mutableListOf()
            }
        }
        if (best.isEmpty()) return null
        val active = best.last().matchId == matches.last().matchId && qualifies(clubId, matches.last(), type)
        return ClubStreak(clubId, clubName, type, best.size, best.first(), best.last(), active, competitionId)
    }

    private fun qualifies(clubId: String, match: HeadToHeadMatch, type: StreakType): Boolean {
        val goalsFor = if (match.homeClubId == clubId) match.homeGoals else match.awayGoals
        val goalsAgainst = if (match.homeClubId == clubId) match.awayGoals else match.homeGoals
        return when (type) {
            StreakType.UNBEATEN -> goalsFor >= goalsAgainst
            StreakType.WINS -> goalsFor > goalsAgainst
            StreakType.LOSSES -> goalsFor < goalsAgainst
            StreakType.WINLESS -> goalsFor <= goalsAgainst
            StreakType.CLEAN_SHEETS -> goalsAgainst == 0
            StreakType.SCORING -> goalsFor > 0
        }
    }
}
