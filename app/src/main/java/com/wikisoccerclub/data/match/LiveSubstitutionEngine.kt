package com.wikisoccerclub.data.match

import com.wikisoccerclub.data.competition.MatchSubstitution

object LiveSubstitutionEngine {

    data class Result(
        val team: LiveTeamState,
        val substitution: MatchSubstitution?,
        val message: String
    )

    fun execute(
        team: LiveTeamState,
        minute: Int,
        playerOutId: String,
        playerInId: String
    ): Result {
        if (team.substitutionsRemaining <= 0) {
            return Result(
                team = team,
                substitution = null,
                message = "O limite de 5 substituições foi atingido."
            )
        }

        val playerOut = team.players.firstOrNull {
            it.player.id == playerOutId && it.onField && !it.substitutedOut
        } ?: return Result(
            team = team,
            substitution = null,
            message = "Jogador de saída inválido."
        )

        val playerIn = team.players.firstOrNull {
            it.player.id == playerInId && !it.onField && !it.substitutedOut
        } ?: return Result(
            team = team,
            substitution = null,
            message = "Jogador de entrada inválido."
        )

        val updatedPlayers = team.players.map { state ->
            when (state.player.id) {
                playerOut.player.id -> state.copy(
                    onField = false,
                    substitutedOut = true
                )
                playerIn.player.id -> state.copy(
                    onField = true,
                    substitutedOut = false
                )
                else -> state
            }
        }

        return Result(
            team = team.copy(
                players = updatedPlayers,
                substitutionsUsed = team.substitutionsUsed + 1
            ),
            substitution = MatchSubstitution(
                minute = minute,
                playerOutId = playerOut.player.id,
                playerInId = playerIn.player.id
            ),
            message = "${playerOut.player.name} saiu e ${playerIn.player.name} entrou."
        )
    }
}
