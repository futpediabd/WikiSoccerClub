package com.wikisoccerclub.data.board

object ReputationEngine {

    fun updateClubReputation(
        reputation: ClubReputation,
        leagueFinish: Int?,
        wonNationalTitle: Boolean,
        wonContinentalTitle: Boolean,
        relegated: Boolean
    ): ClubReputation {
        var nationalChange = 0
        var continentalChange = 0
        var globalChange = 0

        leagueFinish?.let {
            nationalChange += when (it) {
                1 -> 8
                in 2..4 -> 5
                in 5..8 -> 2
                in 15..30 -> -2
                else -> 0
            }
        }

        if (wonNationalTitle) {
            nationalChange += 10
            continentalChange += 2
            globalChange += 1
        }

        if (wonContinentalTitle) {
            nationalChange += 8
            continentalChange += 14
            globalChange += 8
        }

        if (relegated) {
            nationalChange -= 10
            continentalChange -= 4
            globalChange -= 2
        }

        return reputation.copy(
            nationalReputation = (
                reputation.nationalReputation +
                    nationalChange
                ).coerceIn(0, 100),
            continentalReputation = (
                reputation.continentalReputation +
                    continentalChange
                ).coerceIn(0, 100),
            globalReputation = (
                reputation.globalReputation +
                    globalChange
                ).coerceIn(0, 100)
        )
    }

    fun updateManagerReputation(
        reputation: ManagerReputation,
        wonMatch: Boolean,
        wonTrophy: Boolean = false,
        promoted: Boolean = false,
        relegated: Boolean = false
    ): ManagerReputation {
        val matches = reputation.matchesManaged + 1
        val wins = reputation.wins +
            if (wonMatch) 1 else 0

        var change = if (wonMatch) 1 else 0
        if (wonTrophy) change += 10
        if (promoted) change += 7
        if (relegated) change -= 8

        return reputation.copy(
            reputation = (
                reputation.reputation + change
                ).coerceIn(0, 100),
            trophiesWon = reputation.trophiesWon +
                if (wonTrophy) 1 else 0,
            promotions = reputation.promotions +
                if (promoted) 1 else 0,
            relegations = reputation.relegations +
                if (relegated) 1 else 0,
            matchesManaged = matches,
            wins = wins
        )
    }
}
