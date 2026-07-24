package com.wikisoccerclub.data.competition

object AvailabilityEngine {
    private const val YELLOW_LIMIT = 3

    fun calculateForTeam(
        team: CompetitionTeam,
        matches: List<CompetitionMatch>,
        currentRound: Int
    ): TeamAvailability {
        val completedMatches = matches.filter { it.played }

        val rows = team.players.map { player ->
            val playerEvents = completedMatches.flatMap { match ->
                match.events
                    .filter { it.playerId == player.id }
                    .map { event -> match.round to event }
            }

            val injuryReturnRound = playerEvents
                .filter { (_, event) ->
                    event.type == MatchEventType.INJURY
                }
                .map { (round, event) ->
                    round + event.injuryRounds
                }
                .filter { returnRound ->
                    returnRound >= currentRound
                }
                .maxOrNull()

            if (injuryReturnRound != null) {
                return@map PlayerAvailability(
                    player = player,
                    teamName = team.name,
                    status = PlayerAvailabilityStatus.INJURED,
                    reason = "Lesionado",
                    unavailableUntilRound = injuryReturnRound
                )
            }

            val yellowCards = playerEvents.count { (_, event) ->
                event.type == MatchEventType.YELLOW_CARD
            }
            val redCards = playerEvents.count { (_, event) ->
                event.type == MatchEventType.RED_CARD
            }

            val suspensionCount = yellowCards / YELLOW_LIMIT + redCards
            val servedCount = playerEvents.count { (_, event) ->
                event.type == MatchEventType.SUSPENSION_SERVED
            }

            if (suspensionCount > servedCount) {
                return@map PlayerAvailability(
                    player = player,
                    teamName = team.name,
                    status = PlayerAvailabilityStatus.SUSPENDED,
                    reason = if (redCards > 0) {
                        "Suspenso por cartão vermelho"
                    } else {
                        "Suspenso por 3 cartões amarelos"
                    },
                    unavailableUntilRound = currentRound
                )
            }

            PlayerAvailability(
                player = player,
                teamName = team.name,
                status = PlayerAvailabilityStatus.AVAILABLE,
                reason = "Disponível"
            )
        }

        return TeamAvailability(
            team = team,
            players = rows
        )
    }
}
