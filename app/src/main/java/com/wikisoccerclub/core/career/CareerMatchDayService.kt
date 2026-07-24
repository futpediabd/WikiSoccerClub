package com.wikisoccerclub.core.career

import com.wikisoccerclub.data.career.*
import com.wikisoccerclub.data.transfer.CareerDate
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * Coordena a agenda diária: pausa no jogo do usuário e simulação dos demais jogos.
 */
class CareerMatchDayService(
    private val schedule: CareerScheduleRepository,
    private val progression: CareerProgressionService
) {
    fun matchesOn(date: CareerDate): CareerMatchDay {
        val matches = schedule.pendingOn(date)
        return CareerMatchDay(
            date = date,
            matches = matches,
            userMatches = matches.filter { it.involvesUserClub },
            aiMatches = matches.filterNot { it.involvesUserClub }
        )
    }

    fun prepareCurrentDay(): CareerMatchDay {
        val date = progression.currentState().currentDate
        val day = matchesOn(date)
        day.userMatches.forEach { schedule.updateStatus(it.id, ScheduledMatchStatus.READY_FOR_USER) }
        return matchesOn(date)
    }

    fun simulateAiMatches(
        date: CareerDate,
        seed: Int = date.hashCode()
    ): List<SimulatedCareerMatch> {
        val random = Random(seed)
        return matchesOn(date).aiMatches.map { match ->
            val homeAdvantage = 1
            val variation = (match.homeClubId.hashCode() - match.awayClubId.hashCode()).absoluteValue % 3
            val homeGoals = (random.nextInt(0, 4) + homeAdvantage + if (variation == 0) 1 else 0).coerceAtMost(6)
            val awayGoals = (random.nextInt(0, 4) + if (variation == 2) 1 else 0).coerceAtMost(6)
            schedule.complete(match.id, homeGoals, awayGoals)
            SimulatedCareerMatch(match.id, homeGoals, awayGoals)
        }
    }

    fun completeUserMatch(matchId: String, homeGoals: Int, awayGoals: Int): ScheduledCareerMatch? =
        schedule.complete(matchId, homeGoals, awayGoals)

    fun advanceToNextUserMatch(userClubId: String, maximumDays: Int = 370): CareerAdvanceToMatchResult {
        require(maximumDays > 0) { "O limite de dias deve ser positivo." }
        val results = mutableListOf<CareerAdvanceResult>()
        var advanced = 0

        while (advanced < maximumDays) {
            val currentDate = progression.currentState().currentDate
            val currentDay = matchesOn(currentDate)
            if (currentDay.userMatches.any { it.userClubId == userClubId }) {
                currentDay.userMatches.forEach { schedule.updateStatus(it.id, ScheduledMatchStatus.READY_FOR_USER) }
                return CareerAdvanceToMatchResult(currentDate, advanced, matchesOn(currentDate), results)
            }

            simulateAiMatches(currentDate)
            val next = schedule.nextForClub(userClubId, currentDate)
                ?: return CareerAdvanceToMatchResult(currentDate, advanced, null, results)

            val advance = progression.advanceOneDay(
                extraEvents = schedule.pendingOn(com.wikisoccerclub.data.career.CareerDateEngine.nextDay(currentDate))
                    .filter { it.involvesUserClub }
                    .map { match ->
                        progression.registerMatchDay(
                            date = match.date,
                            competition = match.competitionName,
                            opponent = match.opponentName ?: "Adversário",
                            home = match.userIsHome
                        )
                    }
            )
            results += advance
            advanced++
            if (advance.currentState.currentDate > next.date) break
        }

        val reached = progression.currentState().currentDate
        val day = matchesOn(reached).takeIf { it.matches.isNotEmpty() }
        return CareerAdvanceToMatchResult(reached, advanced, day, results)
    }
}
