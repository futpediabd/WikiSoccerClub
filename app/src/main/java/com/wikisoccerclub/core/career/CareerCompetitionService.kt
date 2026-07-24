package com.wikisoccerclub.core.career

import com.wikisoccerclub.data.career.CompetitionRoundSummary
import com.wikisoccerclub.data.career.MatchResultRegistration
import com.wikisoccerclub.data.career.ScheduledCareerMatch
import com.wikisoccerclub.data.career.ScheduledMatchStatus
import com.wikisoccerclub.data.career.CareerScheduleRepository
import com.wikisoccerclub.data.competition.CareerStandingsRepository
import com.wikisoccerclub.data.competition.CompetitionStandings
import com.wikisoccerclub.data.competition.StandingsEngine
import com.wikisoccerclub.data.match.CompletedMatchResult

/**
 * Liga os placares da agenda às classificações e ao controle de rodadas.
 */
class CareerCompetitionService(
    private val schedule: CareerScheduleRepository,
    private val standingsRepository: CareerStandingsRepository
) {
    fun registerResult(
        matchId: String,
        homeGoals: Int,
        awayGoals: Int
    ): MatchResultRegistration? {
        require(homeGoals >= 0 && awayGoals >= 0) { "O placar não pode ser negativo." }

        val original = schedule.all().firstOrNull { it.id == matchId } ?: return null
        val completed = schedule.complete(matchId, homeGoals, awayGoals) ?: return null
        val teamNames = teamNamesForCompetition(original.competitionId)
        val current = standingsRepository.getOrCreate(original.competitionId, teamNames)
        val updated = StandingsEngine.applyResult(
            standings = current,
            result = completed.toCompletedMatchResult(),
            teamNames = teamNames
        ).let(standingsRepository::save)

        return MatchResultRegistration(
            match = completed,
            standings = updated,
            roundCompleted = isRoundCompleted(
                competitionId = completed.competitionId,
                roundLabel = completed.roundLabel
            )
        )
    }

    fun standings(competitionId: String): CompetitionStandings =
        standingsRepository.getOrCreate(competitionId, teamNamesForCompetition(competitionId))

    fun roundSummary(
        competitionId: String,
        roundLabel: String
    ): CompetitionRoundSummary? {
        val matches = schedule.all().filter {
            it.competitionId == competitionId && it.roundLabel == roundLabel
        }
        if (matches.isEmpty()) return null

        val completed = matches.count { it.isFinished() }
        return CompetitionRoundSummary(
            competitionId = competitionId,
            competitionName = matches.first().competitionName,
            roundLabel = roundLabel,
            matches = matches,
            completedMatches = completed,
            pendingMatches = matches.size - completed,
            standings = standings(competitionId)
        )
    }

    fun isRoundCompleted(competitionId: String, roundLabel: String): Boolean {
        if (roundLabel.isBlank()) return false
        val matches = schedule.all().filter {
            it.competitionId == competitionId && it.roundLabel == roundLabel
        }
        return matches.isNotEmpty() && matches.all { it.isFinished() }
    }

    fun nextPendingRound(competitionId: String): String? = schedule.all()
        .filter { it.competitionId == competitionId && !it.isFinished() }
        .firstOrNull()
        ?.roundLabel

    private fun teamNamesForCompetition(competitionId: String): Map<String, String> =
        schedule.all()
            .filter { it.competitionId == competitionId }
            .flatMap { match ->
                listOf(
                    match.homeClubId to match.homeClubName,
                    match.awayClubId to match.awayClubName
                )
            }
            .toMap()

    private fun ScheduledCareerMatch.isFinished(): Boolean =
        status == ScheduledMatchStatus.COMPLETED || status == ScheduledMatchStatus.SIMULATED

    private fun ScheduledCareerMatch.toCompletedMatchResult() = CompletedMatchResult(
        matchId = id,
        homeTeamId = homeClubId,
        awayTeamId = awayClubId,
        homeGoals = homeGoals ?: 0,
        awayGoals = awayGoals ?: 0,
        homeShots = 0,
        awayShots = 0,
        homeShotsOnTarget = 0,
        awayShotsOnTarget = 0,
        events = emptyList(),
        substitutions = emptyList(),
        playerUpdates = emptyList()
    )
}
