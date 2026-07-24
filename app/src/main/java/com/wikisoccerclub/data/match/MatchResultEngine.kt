package com.wikisoccerclub.data.match

object MatchResultEngine {

    fun buildResult(
        matchId: String,
        session: LiveMatchSession
    ): CompletedMatchResult {
        require(session.match.finished) {
            "A partida precisa estar encerrada antes de gerar o resultado."
        }

        val allPlayers = session.homeTeam.players + session.awayTeam.players

        val playerUpdates = allPlayers.map { playerState ->
            val playerEvents = session.match.events.filter {
                it.playerId == playerState.player.id
            }

            PlayerMatchUpdate(
                playerId = playerState.player.id,
                finalEnergy = playerState.energy,
                yellowCards = playerEvents.count {
                    it.type == LiveMatchEventType.YELLOW_CARD
                },
                redCards = playerEvents.count {
                    it.type == LiveMatchEventType.RED_CARD
                },
                injured = playerEvents.any {
                    it.type == LiveMatchEventType.INJURY
                },
                injuryRounds = if (playerEvents.any {
                        it.type == LiveMatchEventType.INJURY
                    }
                ) {
                    injuryDuration(playerState.energy)
                } else {
                    0
                }
            )
        }

        return CompletedMatchResult(
            matchId = matchId,
            homeTeamId = session.homeTeam.teamId,
            awayTeamId = session.awayTeam.teamId,
            homeGoals = session.match.homeScore,
            awayGoals = session.match.awayScore,
            homeShots = session.match.homeShots,
            awayShots = session.match.awayShots,
            homeShotsOnTarget = session.match.homeShotsOnTarget,
            awayShotsOnTarget = session.match.awayShotsOnTarget,
            events = session.match.events,
            substitutions = session.substitutions,
            playerUpdates = playerUpdates
        )
    }

    private fun injuryDuration(energy: Int): Int =
        when {
            energy >= 75 -> 1
            energy >= 55 -> 2
            energy >= 40 -> 3
            else -> 4
        }
}
