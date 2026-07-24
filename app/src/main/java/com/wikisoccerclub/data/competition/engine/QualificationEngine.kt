package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.*
import kotlin.random.Random

class QualificationEngine(private val random: Random = Random.Default) {
    fun qualifyLibertadores(clubs: List<ClubEntry>, firstSeason: Boolean): QualificationResult {
        val selected = linkedMapOf<String,ClubEntry>()
        clubs.groupBy { it.country }.values.forEach { c ->
            c.firstOrNull { it.isLeagueChampion }?.let { selected[it.clubId] = it }
            c.firstOrNull { it.isLeagueRunnerUp }?.let { selected[it.clubId] = it }
            c.firstOrNull { it.isNationalCupChampion }?.let { selected[it.clubId] = it }
        }
        if (firstSeason) {
            addRandomCountry("Brasil", clubs, selected)
            addRandomCountry("Argentina", clubs, selected)
        } else {
            clubs.filter { it.isContinentalChampion }.sortedByDescending { it.level }.take(2).forEach { selected[it.clubId] = it }
        }
        fillByLeague(clubs, selected, 32)
        return QualificationResult(selected.values.take(32), minOf(32, selected.size))
    }

    fun qualifySudamericana(clubs: List<ClubEntry>, firstSeason: Boolean): QualificationResult {
        val selected = linkedMapOf<String,ClubEntry>()
        clubs.filter { it.leaguePosition in 3..5 }.sortedWith(compareBy<ClubEntry>{it.country}.thenBy{it.leaguePosition}).forEach { selected[it.clubId] = it }
        if (firstSeason) {
            addRandomCountry("Brasil", clubs, selected)
            addRandomCountry("Argentina", clubs, selected)
        } else {
            clubs.filter { it.isContinentalRunnerUp }.sortedByDescending { it.level }.take(2).forEach { selected[it.clubId] = it }
        }
        fillByLeague(clubs, selected, 32)
        return QualificationResult(selected.values.take(32), minOf(32, selected.size))
    }

    fun qualifyContinental64(clubs: List<ClubEntry>): QualificationResult {
        val selected = linkedMapOf<String,ClubEntry>()
        clubs.groupBy { it.country }.values.forEach { c -> c.firstOrNull { it.isLeagueChampion }?.let { selected[it.clubId] = it } }
        clubs.filter { it.isLeagueRunnerUp }.sortedByDescending { it.level }.forEach { if(selected.size<64) selected[it.clubId]=it }
        fillByLeague(clubs, selected, 64)
        return QualificationResult(selected.values.take(64), minOf(64, selected.size))
    }

    fun qualifyOceania32(clubs: List<ClubEntry>): QualificationResult {
        val selected = linkedMapOf<String,ClubEntry>()
        listOf(1,2,3).forEach { p -> clubs.filter { it.leaguePosition == p }.sortedByDescending { it.level }.forEach { if(selected.size<32) selected[it.clubId]=it } }
        return QualificationResult(selected.values.take(32), minOf(32, selected.size))
    }

    fun qualifySuperWorldCup(clubs: List<ClubEntry>, targetSize: Int): QualificationResult {
        require(targetSize in setOf(32,64,128,256))
        val selected = linkedMapOf<String,ClubEntry>()
        clubs.filter { it.isContinentalChampion }.sortedByDescending { it.level }.forEach { selected[it.clubId]=it }
        clubs.groupBy { it.country }.values.forEach { c -> c.firstOrNull { it.isLeagueChampion }?.let { selected[it.clubId]=it } }
        clubs.filter { it.isContinentalChampion }.forEach { champion ->
            val country = clubs.filter { it.country == champion.country }
            val leagueChampion = country.firstOrNull { it.isLeagueChampion }
            if (leagueChampion?.clubId == champion.clubId) country.firstOrNull { it.isLeagueRunnerUp }?.let { selected[it.clubId]=it }
            else leagueChampion?.let { selected[it.clubId]=it }
        }
        fillByLeague(clubs, selected, targetSize)
        clubs.filter { it.isNationalCupChampion }.sortedByDescending { it.level }.forEach { if(selected.size<targetSize) selected[it.clubId]=it }
        return QualificationResult(selected.values.take(targetSize), minOf(targetSize, selected.size))
    }

    private fun fillByLeague(clubs: List<ClubEntry>, selected: MutableMap<String,ClubEntry>, target: Int) {
        clubs.filter { it.leaguePosition != null }.sortedWith(compareBy<ClubEntry>{it.leaguePosition ?: Int.MAX_VALUE}.thenByDescending{it.level}).forEach { if(selected.size<target) selected[it.clubId]=it }
    }

    private fun addRandomCountry(country: String, clubs: List<ClubEntry>, selected: MutableMap<String,ClubEntry>) {
        clubs.filter { it.country.equals(country,true) && !selected.containsKey(it.clubId) }.shuffled(random).firstOrNull()?.let { selected[it.clubId]=it }
    }
}
