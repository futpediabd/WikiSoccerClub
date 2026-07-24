package com.wikisoccerclub.data.match

import kotlin.random.Random

class MinuteMatchEngine(
    private val random: Random = Random.Default
) {
    fun advance(
        state: LiveMatchState,
        homeTeamId: String,
        awayTeamId: String
    ): LiveMatchState {
        if (state.finished || state.paused || state.pausedForSubstitution) {
            return state
        }

        val nextMinute = (state.currentMinute + 1).coerceAtMost(90)

        var updated = state.copy(currentMinute = nextMinute)

        if (nextMinute == 1 && state.events.none {
                it.type == LiveMatchEventType.KICK_OFF
            }
        ) {
            updated = updated.withEvent(
                LiveMatchEvent(
                    id = "kickoff",
                    minute = 1,
                    type = LiveMatchEventType.KICK_OFF,
                    description = "A partida começou."
                )
            )
        }

        updated = generateMinuteEvent(
            state = updated,
            homeTeamId = homeTeamId,
            awayTeamId = awayTeamId
        )

        if (nextMinute == 45 && !updated.halfTimeCompleted) {
            return updated
                .withEvent(
                    LiveMatchEvent(
                        id = "half_time",
                        minute = 45,
                        type = LiveMatchEventType.HALF_TIME,
                        description = "Fim do primeiro tempo."
                    )
                )
                .copy(pausedForSubstitution = true)
        }

        if (nextMinute >= 90) {
            return updated
                .withEvent(
                    LiveMatchEvent(
                        id = "full_time",
                        minute = 90,
                        type = LiveMatchEventType.FULL_TIME,
                        description = "Fim de jogo."
                    )
                )
                .copy(finished = true)
        }

        return updated
    }

    fun resumeAfterHalfTime(state: LiveMatchState): LiveMatchState =
        state.copy(
            pausedForSubstitution = false,
            halfTimeCompleted = true
        )

    private fun generateMinuteEvent(
        state: LiveMatchState,
        homeTeamId: String,
        awayTeamId: String
    ): LiveMatchState {
        val roll = random.nextInt(100)
        if (roll > 15) return state

        val homeAction = random.nextBoolean()
        val teamId = if (homeAction) homeTeamId else awayTeamId

        return when {
            roll <= 2 -> registerGoal(state, teamId, homeTeamId)
            roll <= 7 -> registerShotOnTarget(state, teamId, homeTeamId)
            roll <= 12 -> registerShot(state, teamId, homeTeamId)
            roll == 13 -> state.withEvent(
                LiveMatchEvent(
                    id = "yellow_${state.currentMinute}_${state.events.size}",
                    minute = state.currentMinute,
                    type = LiveMatchEventType.YELLOW_CARD,
                    teamId = teamId,
                    description = "Cartão amarelo."
                )
            )
            roll == 14 -> state.withEvent(
                LiveMatchEvent(
                    id = "injury_${state.currentMinute}_${state.events.size}",
                    minute = state.currentMinute,
                    type = LiveMatchEventType.INJURY,
                    teamId = teamId,
                    description = "Jogador sente uma lesão."
                )
            )
            else -> state
        }
    }

    private fun registerGoal(
        state: LiveMatchState,
        teamId: String,
        homeTeamId: String
    ): LiveMatchState {
        val isHome = teamId == homeTeamId

        return state.copy(
            homeScore = state.homeScore + if (isHome) 1 else 0,
            awayScore = state.awayScore + if (isHome) 0 else 1,
            homeShots = state.homeShots + if (isHome) 1 else 0,
            awayShots = state.awayShots + if (isHome) 0 else 1,
            homeShotsOnTarget = state.homeShotsOnTarget + if (isHome) 1 else 0,
            awayShotsOnTarget = state.awayShotsOnTarget + if (isHome) 0 else 1
        ).withEvent(
            LiveMatchEvent(
                id = "goal_${state.currentMinute}_${state.events.size}",
                minute = state.currentMinute,
                type = LiveMatchEventType.GOAL,
                teamId = teamId,
                description = "Gol!"
            )
        )
    }

    private fun registerShotOnTarget(
        state: LiveMatchState,
        teamId: String,
        homeTeamId: String
    ): LiveMatchState {
        val isHome = teamId == homeTeamId

        return state.copy(
            homeShots = state.homeShots + if (isHome) 1 else 0,
            awayShots = state.awayShots + if (isHome) 0 else 1,
            homeShotsOnTarget = state.homeShotsOnTarget + if (isHome) 1 else 0,
            awayShotsOnTarget = state.awayShotsOnTarget + if (isHome) 0 else 1
        ).withEvent(
            LiveMatchEvent(
                id = "target_${state.currentMinute}_${state.events.size}",
                minute = state.currentMinute,
                type = LiveMatchEventType.SHOT_ON_TARGET,
                teamId = teamId,
                description = "Finalização no gol."
            )
        )
    }

    private fun registerShot(
        state: LiveMatchState,
        teamId: String,
        homeTeamId: String
    ): LiveMatchState {
        val isHome = teamId == homeTeamId

        return state.copy(
            homeShots = state.homeShots + if (isHome) 1 else 0,
            awayShots = state.awayShots + if (isHome) 0 else 1
        ).withEvent(
            LiveMatchEvent(
                id = "shot_${state.currentMinute}_${state.events.size}",
                minute = state.currentMinute,
                type = LiveMatchEventType.SHOT,
                teamId = teamId,
                description = "Finalização para fora."
            )
        )
    }

    private fun LiveMatchState.withEvent(
        event: LiveMatchEvent
    ): LiveMatchState =
        copy(events = events + event)
}
