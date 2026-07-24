package com.wikisoccerclub.data.competition

enum class KnockoutRoundType {
    PRELIMINARY,
    ROUND_OF_128,
    ROUND_OF_64,
    ROUND_OF_32,
    ROUND_OF_16,
    QUARTER_FINAL,
    SEMI_FINAL,
    FINAL
}

data class KnockoutTie(
    val id: String,
    val round: KnockoutRoundType,
    val homeTeamId: String,
    val awayTeamId: String,
    val firstLegMatchId: String,
    val secondLegMatchId: String? = null,
    val firstLegHomeGoals: Int? = null,
    val firstLegAwayGoals: Int? = null,
    val secondLegHomeGoals: Int? = null,
    val secondLegAwayGoals: Int? = null,
    val penaltiesHome: Int? = null,
    val penaltiesAway: Int? = null,
    val winnerTeamId: String? = null,
    val completed: Boolean = false
)

data class KnockoutCompetitionProgress(
    val competitionId: String,
    val currentRound: KnockoutRoundType,
    val ties: List<KnockoutTie>,
    val championTeamId: String? = null,
    val completed: Boolean = false
)
