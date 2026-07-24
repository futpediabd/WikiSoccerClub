package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.*
import kotlin.random.Random

class LiveDrawEngine(private val random: Random = Random.Default) {
    fun drawKnockout(clubs: List<ClubEntry>, lowerLevelHostsFirst: Boolean): List<DrawPairing> {
        require(clubs.size % 2 == 0) { "O sorteio exige quantidade par de clubes." }
        val pot = clubs.shuffled(random).toMutableList()
        val result = mutableListOf<DrawPairing>()
        while (pot.isNotEmpty()) {
            val a = pot.removeAt(0)
            val b = pot.removeAt(0)
            if (lowerLevelHostsFirst) {
                val home = when { a.level < b.level -> a; b.level < a.level -> b; else -> listOf(a,b).random(random) }
                result += DrawPairing(home, if (home.clubId == a.clubId) b else a)
            } else result += DrawPairing(a,b)
        }
        return result
    }

    fun drawGroups(clubs: List<ClubEntry>, groups: Int, clubsPerGroup: Int = 4, maxSameCountry: Int = 2): Map<Char,List<ClubEntry>> {
        require(clubs.size == groups * clubsPerGroup)
        repeat(2000) {
            val map = linkedMapOf<Char,MutableList<ClubEntry>>()
            repeat(groups) { i -> map[('A'.code+i).toChar()] = mutableListOf() }
            var ok = true
            for (club in clubs.shuffled(random)) {
                val possible = map.filterValues { g -> g.size < clubsPerGroup && g.count { it.country == club.country } < maxSameCountry }.keys.shuffled(random)
                val key = possible.firstOrNull()
                if (key == null) { ok = false; break }
                map.getValue(key).add(club)
            }
            if (ok && map.values.all { it.size == clubsPerGroup }) return map.mapValues { it.value.toList() }
        }
        error("Não foi possível montar os grupos respeitando o limite por país.")
    }
}
