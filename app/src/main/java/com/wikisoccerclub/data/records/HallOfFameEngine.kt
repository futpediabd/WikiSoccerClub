package com.wikisoccerclub.data.records

object HallOfFameEngine {

    fun playerScore(
        stats: PlayerCareerStats
    ): Int = (
        stats.matches / 10 +
            stats.goals * 2 +
            stats.assists +
            stats.cleanSheets +
            stats.titles * 25
        ).coerceAtLeast(0)

    fun clubScore(
        stats: ClubCareerStats
    ): Int = (
        stats.wins +
            stats.goalsFor / 5 +
            stats.titles * 40
        ).coerceAtLeast(0)

    fun createPlayerEntry(
        stats: PlayerCareerStats,
        inductionYear: Int,
        minimumScore: Int = 100
    ): HallOfFameEntry? {
        val score = playerScore(stats)
        if (score < minimumScore) return null

        return HallOfFameEntry(
            id = "hof_player_${stats.playerId}",
            type = HallOfFameType.PLAYER,
            subjectId = stats.playerId,
            subjectName = stats.playerName,
            inductionYear = inductionYear,
            score = score,
            titles = stats.titles,
            appearances = stats.matches,
            goals = stats.goals,
            description =
                "Ídolo reconhecido por sua trajetória no futebol."
        )
    }

    fun createClubEntry(
        stats: ClubCareerStats,
        inductionYear: Int,
        minimumScore: Int = 200
    ): HallOfFameEntry? {
        val score = clubScore(stats)
        if (score < minimumScore) return null

        return HallOfFameEntry(
            id = "hof_club_${stats.clubId}",
            type = HallOfFameType.CLUB,
            subjectId = stats.clubId,
            subjectName = stats.clubName,
            inductionYear = inductionYear,
            score = score,
            titles = stats.titles,
            appearances = stats.matches,
            description =
                "Clube histórico reconhecido por suas conquistas."
        )
    }
}
