package com.wikisoccerclub.core.headtohead

import com.wikisoccerclub.data.career.ScheduledCareerMatch
import com.wikisoccerclub.data.headtohead.*
import kotlin.math.abs

class HeadToHeadService(
    private val repository: HeadToHeadRepository
) {
    fun registerCompletedMatch(match: ScheduledCareerMatch): HeadToHeadMatch? {
        val homeGoals = match.homeGoals ?: return null
        val awayGoals = match.awayGoals ?: return null
        return repository.save(
            HeadToHeadMatch(
                matchId = match.id,
                date = match.date,
                competitionId = match.competitionId,
                competitionName = match.competitionName,
                homeClubId = match.homeClubId,
                homeClubName = match.homeClubName,
                awayClubId = match.awayClubId,
                awayClubName = match.awayClubName,
                homeGoals = homeGoals,
                awayGoals = awayGoals
            )
        )
    }

    fun summary(
        firstClubId: String,
        secondClubId: String,
        competitionId: String? = null,
        recentLimit: Int = 10
    ): HeadToHeadSummary? {
        require(firstClubId != secondClubId) { "Selecione dois clubes diferentes." }
        require(recentLimit > 0) { "O limite de partidas recentes deve ser positivo." }

        val matches = repository.between(firstClubId, secondClubId)
            .filter { competitionId == null || it.competitionId == competitionId }
        if (matches.isEmpty()) return null

        return HeadToHeadSummary(
            firstClub = buildClubSummary(firstClubId, secondClubId, matches, recentLimit),
            secondClub = buildClubSummary(secondClubId, firstClubId, matches, recentLimit),
            competitionFilter = competitionId,
            allMatches = matches
        )
    }

    private fun buildClubSummary(
        clubId: String,
        opponentId: String,
        matches: List<HeadToHeadMatch>,
        recentLimit: Int
    ): ClubHeadToHeadSummary {
        val first = matches.first()
        val clubName = nameOf(clubId, first)
        val opponentName = nameOf(opponentId, first)
        val wins = matches.count { goalsFor(clubId, it) > goalsAgainst(clubId, it) }
        val draws = matches.count { it.homeGoals == it.awayGoals }
        val homeMatches = matches.filter { it.homeClubId == clubId }
        val awayMatches = matches.filter { it.awayClubId == clubId }

        return ClubHeadToHeadSummary(
            clubId = clubId,
            clubName = clubName,
            opponentId = opponentId,
            opponentName = opponentName,
            matches = matches.size,
            wins = wins,
            draws = draws,
            losses = matches.size - wins - draws,
            goalsFor = matches.sumOf { goalsFor(clubId, it) },
            goalsAgainst = matches.sumOf { goalsAgainst(clubId, it) },
            homeMatches = homeMatches.size,
            awayMatches = awayMatches.size,
            homeWins = homeMatches.count { it.homeGoals > it.awayGoals },
            awayWins = awayMatches.count { it.awayGoals > it.homeGoals },
            records = ClubVenueRecord(
                biggestHomeWin = bestRecord(clubId, homeMatches, victory = true),
                biggestAwayWin = bestRecord(clubId, awayMatches, victory = true),
                biggestHomeDefeat = bestRecord(clubId, homeMatches, victory = false),
                biggestAwayDefeat = bestRecord(clubId, awayMatches, victory = false)
            ),
            recentMatches = matches.take(recentLimit)
        )
    }

    private fun bestRecord(
        clubId: String,
        matches: List<HeadToHeadMatch>,
        victory: Boolean
    ): HeadToHeadScoreRecord? = matches
        .filter {
            val difference = goalsFor(clubId, it) - goalsAgainst(clubId, it)
            if (victory) difference > 0 else difference < 0
        }
        .maxWithOrNull(
            compareBy<HeadToHeadMatch> { abs(goalsFor(clubId, it) - goalsAgainst(clubId, it)) }
                .thenBy { goalsFor(clubId, it) }
                .thenBy { it.date }
        )
        ?.toRecord(clubId)

    private fun goalsFor(clubId: String, match: HeadToHeadMatch): Int =
        if (match.homeClubId == clubId) match.homeGoals else match.awayGoals

    private fun goalsAgainst(clubId: String, match: HeadToHeadMatch): Int =
        if (match.homeClubId == clubId) match.awayGoals else match.homeGoals

    private fun nameOf(clubId: String, match: HeadToHeadMatch): String =
        if (match.homeClubId == clubId) match.homeClubName else match.awayClubName

    private fun HeadToHeadMatch.toRecord(clubId: String) = HeadToHeadScoreRecord(
        matchId = matchId,
        date = date,
        competitionName = competitionName,
        homeClubName = homeClubName,
        awayClubName = awayClubName,
        homeGoals = homeGoals,
        awayGoals = awayGoals,
        goalDifference = abs(goalsFor(clubId, this) - goalsAgainst(clubId, this))
    )
}
