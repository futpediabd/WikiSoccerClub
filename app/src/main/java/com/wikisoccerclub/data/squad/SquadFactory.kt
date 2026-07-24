package com.wikisoccerclub.data.squad

import com.wikisoccerclub.data.ban.BanClub

object SquadFactory {

    fun fromClub(club: BanClub?): List<SquadPlayer> {
        val imported = club?.players.orEmpty().mapIndexed { index, player ->
            SquadPlayer(
                id = "ban_$index",
                name = player.name.ifBlank { "Jogador ${index + 1}" },
                position = player.position.ifBlank { defaultPosition(index) },
                overall = player.overall
            )
        }

        if (imported.size >= 18) return imported.take(35)

        val generated = (imported.size until 22).map { index ->
            SquadPlayer(
                id = "generated_$index",
                name = "Jogador ${index + 1}",
                position = defaultPosition(index),
                overall = 58 + (index % 16)
            )
        }

        return (imported + generated).take(35)
    }

    fun automaticLineup(players: List<SquadPlayer>): MatchLineup {
        val ordered = players.sortedByDescending { it.overall }
        return MatchLineup(
            starters = ordered.take(11).map { it.copy(starter = true, bench = false) },
            bench = ordered.drop(11).take(7).map { it.copy(starter = false, bench = true) }
        )
    }

    private fun defaultPosition(index: Int): String = when (index) {
        0, 1 -> "GOL"
        2, 3, 4, 5, 6, 7 -> "DEF"
        8, 9, 10, 11, 12, 13, 14 -> "MEI"
        else -> "ATA"
    }
}
