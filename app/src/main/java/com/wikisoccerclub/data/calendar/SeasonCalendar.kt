package com.wikisoccerclub.data.calendar

data class MatchEvent(
    val id: Int,
    val competition: String,
    val round: String,
    val home: String,
    val away: String
) {
    fun isUserHome(userClub: String): Boolean = home == userClub
    fun opponent(userClub: String): String = if (isUserHome(userClub)) away else home
}

object SeasonCalendar {
    fun generate(userClub: String): List<MatchEvent> = listOf(
        MatchEvent(1, "Campeonato Estadual", "1ª Rodada", userClub, "Atlético Central"),
        MatchEvent(2, "Campeonato Estadual", "2ª Rodada", "União Esportiva", userClub),
        MatchEvent(3, "Campeonato Estadual", "3ª Rodada", userClub, "Nacional da Serra"),
        MatchEvent(4, "Campeonato Estadual", "4ª Rodada", "Ferroviário Azul", userClub),
        MatchEvent(5, "Campeonato Estadual", "5ª Rodada", userClub, "Real Municipal")
    )

    fun nextMatch(userClub: String, currentEvent: Int): MatchEvent? {
        return generate(userClub).firstOrNull { it.id > currentEvent }
    }
}
