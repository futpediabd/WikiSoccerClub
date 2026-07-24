package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.ClubCompetitionHistory
import com.wikisoccerclub.data.competition.model.CompetitionMatchResult

object CompetitionHistoryEngine {

    fun updateClubHistory(
        current: ClubCompetitionHistory,
        matches: List<CompetitionMatchResult>,
        championClubId: String,
        runnerUpClubId: String,
        thirdPlaceClubId: String? = null,
        fourthPlaceClubId: String? = null
    ): ClubCompetitionHistory {
        val clubMatches = matches.filter {
            it.homeClubId == current.clubId ||
                it.awayClubId == current.clubId
        }

        var wins = 0
        var draws = 0
        var losses = 0
        var goalsFor = 0
        var goalsAgainst = 0

        clubMatches.forEach { match ->
            val isHome = match.homeClubId == current.clubId
            val scored = if (isHome) match.homeGoals else match.awayGoals
            val conceded = if (isHome) match.awayGoals else match.homeGoals

            goalsFor += scored
            goalsAgainst += conceded

            when {
                scored > conceded -> wins++
                scored == conceded -> draws++
                else -> losses++
            }
        }

        return current.copy(
            participations = current.participations + 1,
            titles = current.titles +
                if (current.clubId == championClubId) 1 else 0,
            runnerUps = current.runnerUps +
                if (current.clubId == runnerUpClubId) 1 else 0,
            thirdPlaces = current.thirdPlaces +
                if (current.clubId == thirdPlaceClubId) 1 else 0,
            fourthPlaces = current.fourthPlaces +
                if (current.clubId == fourthPlaceClubId) 1 else 0,
            matches = current.matches + clubMatches.size,
            wins = current.wins + wins,
            draws = current.draws + draws,
            losses = current.losses + losses,
            goalsFor = current.goalsFor + goalsFor,
            goalsAgainst = current.goalsAgainst + goalsAgainst
        )
    }
}
