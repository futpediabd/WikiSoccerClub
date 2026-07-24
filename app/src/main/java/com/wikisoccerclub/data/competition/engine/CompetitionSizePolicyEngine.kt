package com.wikisoccerclub.data.competition.engine

object CompetitionSizePolicyEngine {

    fun resolveNationalCupSize(clubCount: Int): Int? =
        when {
            clubCount >= 256 -> 256
            clubCount >= 128 -> 128
            clubCount >= 64 -> 64
            clubCount >= 32 -> 32
            clubCount >= 16 -> 16
            else -> null
        }

    fun resolveSuperWorldCupSize(clubCount: Int): Int? =
        when {
            clubCount >= 256 -> 256
            clubCount >= 128 -> 128
            clubCount >= 64 -> 64
            clubCount >= 32 -> 32
            else -> null
        }

    fun validateStateChampionshipSize(clubCount: Int): Boolean =
        clubCount >= 16
}
