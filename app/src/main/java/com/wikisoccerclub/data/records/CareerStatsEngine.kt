package com.wikisoccerclub.data.records

object CareerStatsEngine {

    fun registerPlayerMatch(
        stats: PlayerCareerStats,
        started: Boolean,
        goals: Int,
        assists: Int,
        cleanSheet: Boolean,
        yellowCards: Int = 0,
        redCards: Int = 0
    ): PlayerCareerStats {
        require(goals >= 0)
        require(assists >= 0)
        require(yellowCards >= 0)
        require(redCards >= 0)

        return stats.copy(
            matches = stats.matches + 1,
            starts = stats.starts + if (started) 1 else 0,
            goals = stats.goals + goals,
            assists = stats.assists + assists,
            cleanSheets = stats.cleanSheets +
                if (cleanSheet) 1 else 0,
            yellowCards =
                stats.yellowCards + yellowCards,
            redCards =
                stats.redCards + redCards
        )
    }

    fun addPlayerTitle(
        stats: PlayerCareerStats
    ): PlayerCareerStats =
        stats.copy(titles = stats.titles + 1)

    fun registerClubMatch(
        stats: ClubCareerStats,
        goalsFor: Int,
        goalsAgainst: Int,
        attendance: Int = 0
    ): ClubCareerStats {
        require(goalsFor >= 0)
        require(goalsAgainst >= 0)
        require(attendance >= 0)

        return stats.copy(
            matches = stats.matches + 1,
            wins = stats.wins +
                if (goalsFor > goalsAgainst) 1 else 0,
            draws = stats.draws +
                if (goalsFor == goalsAgainst) 1 else 0,
            losses = stats.losses +
                if (goalsFor < goalsAgainst) 1 else 0,
            goalsFor = stats.goalsFor + goalsFor,
            goalsAgainst =
                stats.goalsAgainst + goalsAgainst,
            highestAttendance =
                maxOf(stats.highestAttendance, attendance)
        )
    }

    fun addClubTitle(
        stats: ClubCareerStats
    ): ClubCareerStats =
        stats.copy(titles = stats.titles + 1)

    fun registerTransferFee(
        stats: ClubCareerStats,
        fee: Long
    ): ClubCareerStats {
        require(fee >= 0)
        return stats.copy(
            biggestTransferFee =
                maxOf(stats.biggestTransferFee, fee)
        )
    }
}
