package com.wikisoccerclub.data.competition

object LineupEditor {

    fun moveToStarters(
        lineup: CompetitionLineup,
        player: CompetitionPlayer
    ): CompetitionLineup {
        if (lineup.starters.any { it.id == player.id }) return lineup

        val newSubstitutes = lineup.substitutes
            .filterNot { it.id == player.id }

        if (lineup.starters.size >= 11) return lineup

        return lineup.copy(
            starters = lineup.starters + player,
            substitutes = newSubstitutes
        )
    }

    fun moveToSubstitutes(
        lineup: CompetitionLineup,
        player: CompetitionPlayer
    ): CompetitionLineup {
        if (lineup.substitutes.any { it.id == player.id }) return lineup

        val newStarters = lineup.starters
            .filterNot { it.id == player.id }

        if (lineup.substitutes.size >= 7) return lineup

        return lineup.copy(
            starters = newStarters,
            substitutes = lineup.substitutes + player
        )
    }

    fun removePlayer(
        lineup: CompetitionLineup,
        playerId: String
    ): CompetitionLineup =
        lineup.copy(
            starters = lineup.starters.filterNot { it.id == playerId },
            substitutes = lineup.substitutes.filterNot { it.id == playerId }
        )

    fun swapPlayers(
        lineup: CompetitionLineup,
        starterId: String,
        substituteId: String
    ): CompetitionLineup {
        val starter = lineup.starters.firstOrNull {
            it.id == starterId
        } ?: return lineup

        val substitute = lineup.substitutes.firstOrNull {
            it.id == substituteId
        } ?: return lineup

        return lineup.copy(
            starters = lineup.starters.map {
                if (it.id == starter.id) substitute else it
            },
            substitutes = lineup.substitutes.map {
                if (it.id == substitute.id) starter else it
            }
        )
    }

    fun validate(lineup: CompetitionLineup): LineupValidation {
        if (lineup.starters.size != 11) {
            return LineupValidation(
                valid = false,
                message = "A escalação precisa ter 11 titulares."
            )
        }

        if (lineup.substitutes.size > 7) {
            return LineupValidation(
                valid = false,
                message = "O banco pode ter no máximo 7 reservas."
            )
        }

        if (lineup.starters.none { it.position == "GOL" }) {
            return LineupValidation(
                valid = false,
                message = "A escalação precisa ter pelo menos um goleiro."
            )
        }

        val duplicated = (lineup.starters + lineup.substitutes)
            .groupingBy { it.id }
            .eachCount()
            .any { it.value > 1 }

        if (duplicated) {
            return LineupValidation(
                valid = false,
                message = "Há jogadores duplicados na escalação."
            )
        }

        return LineupValidation(
            valid = true,
            message = "Escalação válida."
        )
    }
}
