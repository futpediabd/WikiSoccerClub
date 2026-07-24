package com.wikisoccerclub.data.competition

import kotlin.math.max
import kotlin.random.Random

class CompetitionSimulator(
    private val random: Random = Random.Default
) {
    fun simulate(
        match: CompetitionMatch,
        teamsById: Map<String, CompetitionTeam>
    ): CompetitionMatch {
        val home = teamsById[match.homeTeamId] ?: return match
        val away = teamsById[match.awayTeamId] ?: return match
        val homeGoals = generateGoals(home.strength + 6, away.strength)
        val awayGoals = generateGoals(away.strength, home.strength)
        val homeShots = generateShots(home.strength + 6, away.strength, homeGoals)
        val awayShots = generateShots(away.strength, home.strength, awayGoals)
        val homePossession = (50 + ((home.strength + 6) - away.strength) / 3 + random.nextInt(-5, 6)).coerceIn(35, 65)

        val events = buildList {
            repeat(homeGoals) { add(createGoalEvent(home)) }
            repeat(awayGoals) { add(createGoalEvent(away)) }
            addAll(createCardEvents(home))
            addAll(createCardEvents(away))
            createInjuryEvent(home)?.let(::add)
            createInjuryEvent(away)?.let(::add)
        }.sortedBy { it.minute }

        return match.copy(
            homeGoals = homeGoals,
            awayGoals = awayGoals,
            homeShots = homeShots,
            awayShots = awayShots,
            homeShotsOnTarget = generateShotsOnTarget(homeShots, homeGoals),
            awayShotsOnTarget = generateShotsOnTarget(awayShots, awayGoals),
            homePossession = homePossession,
            awayPossession = 100 - homePossession,
            played = true,
            events = events
        )
    }

    private fun createGoalEvent(team: CompetitionTeam): MatchEvent {
        val scorer = team.players.flatMap { p -> List((p.finishing / 10).coerceAtLeast(1)) { p } }.random(random)
        return MatchEvent("goal_${team.id}_${random.nextInt(1_000_000)}", MatchEventType.GOAL, scorer.id, team.id, random.nextInt(1, 91))
    }

    private fun createCardEvents(team: CompetitionTeam): List<MatchEvent> {
        val events = mutableListOf<MatchEvent>()
        team.players.forEach { player ->
            if (random.nextInt(100) < (72 - player.discipline).coerceIn(3, 25)) {
                events += MatchEvent("yellow_${player.id}_${random.nextInt(1_000_000)}", MatchEventType.YELLOW_CARD, player.id, team.id, random.nextInt(5, 91))
            }
            if (random.nextInt(500) < (48 - player.discipline / 2).coerceIn(1, 7)) {
                events += MatchEvent("red_${player.id}_${random.nextInt(1_000_000)}", MatchEventType.RED_CARD, player.id, team.id, random.nextInt(20, 91))
            }
        }
        return events.take(5)
    }

    private fun createInjuryEvent(team: CompetitionTeam): MatchEvent? {
        if (random.nextInt(100) >= 12) return null
        val player = team.players.sortedBy { it.fitness }.take(5).randomOrNull(random) ?: return null
        val duration = when (random.nextInt(100)) {
            in 0..54 -> 1
            in 55..84 -> 2
            in 85..96 -> 3
            else -> 5
        }
        return MatchEvent("injury_${player.id}_${random.nextInt(1_000_000)}", MatchEventType.INJURY, player.id, team.id, random.nextInt(10, 86), duration)
    }

    private fun generateGoals(attack: Int, defense: Int): Int {
        val difference = attack - defense
        var total = 0
        repeat(max(3, 7 + difference / 12)) {
            if (random.nextInt(100) < (18 + difference / 3).coerceIn(7, 42)) total++
        }
        return total.coerceAtMost(7)
    }

    private fun generateShots(attack: Int, defense: Int, goals: Int): Int =
        (8 + (attack - defense) / 8 + random.nextInt(0, 8)).coerceAtLeast(goals + 2).coerceAtMost(28)

    private fun generateShotsOnTarget(shots: Int, goals: Int): Int =
        (goals + random.nextInt(1, max(2, shots / 2 + 1))).coerceAtMost(shots)
}
