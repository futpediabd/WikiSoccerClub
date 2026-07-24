package com.wikisoccerclub.data.youth

data class YouthTryoutSettings(
    val allPositionsCost: Long = 150_000L,
    val goalkeeperCost: Long = 90_000L,
    val defenderCost: Long = 100_000L,
    val midfielderCost: Long = 110_000L,
    val attackerCost: Long = 120_000L,
    val cooldownDays: Int = 30,
    val resultValidityDays: Int = 14,
    val minimumCandidates: Int = 4,
    val maximumCandidates: Int = 8,
    val localNationalityChance: Int = 70
) {
    fun costFor(
        position: YouthPositionFilter
    ): Long =
        when (position) {
            YouthPositionFilter.ALL ->
                allPositionsCost
            YouthPositionFilter.GOALKEEPER ->
                goalkeeperCost
            YouthPositionFilter.DEFENDER ->
                defenderCost
            YouthPositionFilter.MIDFIELDER ->
                midfielderCost
            YouthPositionFilter.ATTACKER ->
                attackerCost
        }
}
