package com.wikisoccerclub.data.rivalry

import kotlin.math.abs

object RivalryEngine {

    fun rivalryId(
        clubAId: String,
        clubBId: String
    ): String {
        require(clubAId.isNotBlank())
        require(clubBId.isNotBlank())
        require(clubAId != clubBId)

        return listOf(clubAId, clubBId)
            .sorted()
            .joinToString("_")
    }

    fun create(
        input: RivalryMatchInput,
        scope: RivalryScope
    ): ClubRivalry =
        ClubRivalry(
            id = rivalryId(
                input.clubAId,
                input.clubBId
            ),
            clubAId = input.clubAId,
            clubAName = input.clubAName,
            clubBId = input.clubBId,
            clubBName = input.clubBName,
            scope = scope
        )

    fun registerMatch(
        rivalry: ClubRivalry,
        input: RivalryMatchInput
    ): ClubRivalry {
        validateMatch(rivalry, input)

        val normalized = normalizeInput(
            rivalry = rivalry,
            input = input
        )

        val scoreIncrease =
            calculateScoreIncrease(normalized)

        return rivalry.copy(
            score = (
                rivalry.score + scoreIncrease
                ).coerceIn(0, 100),
            matches = rivalry.matches + 1,
            clubAWins = rivalry.clubAWins +
                if (
                    normalized.clubAGoals >
                    normalized.clubBGoals
                ) 1 else 0,
            draws = rivalry.draws +
                if (
                    normalized.clubAGoals ==
                    normalized.clubBGoals
                ) 1 else 0,
            clubBWins = rivalry.clubBWins +
                if (
                    normalized.clubBGoals >
                    normalized.clubAGoals
                ) 1 else 0,
            clubAGoals =
                rivalry.clubAGoals +
                    normalized.clubAGoals,
            clubBGoals =
                rivalry.clubBGoals +
                    normalized.clubBGoals,
            finalsPlayed = rivalry.finalsPlayed +
                if (normalized.isFinal) 1 else 0,
            titleDecisions = rivalry.titleDecisions +
                if (normalized.decidedTitle) 1 else 0,
            lastSeasonYear = normalized.seasonYear
        )
    }

    fun decay(
        rivalry: ClubRivalry,
        currentSeasonYear: Int
    ): ClubRivalry {
        val lastYear =
            rivalry.lastSeasonYear ?: return rivalry
        val seasonsWithoutMeeting =
            (currentSeasonYear - lastYear)
                .coerceAtLeast(0)

        if (seasonsWithoutMeeting <= 1) {
            return rivalry
        }

        val decay = when (rivalry.intensity) {
            RivalryIntensity.HISTORIC -> 0
            RivalryIntensity.FIERCE -> seasonsWithoutMeeting / 3
            RivalryIntensity.HIGH -> seasonsWithoutMeeting / 2
            RivalryIntensity.MODERATE ->
                seasonsWithoutMeeting
            RivalryIntensity.LOW ->
                seasonsWithoutMeeting * 2
        }

        return rivalry.copy(
            score = (rivalry.score - decay)
                .coerceAtLeast(0)
        )
    }

    fun matchIntensityBonus(
        rivalry: ClubRivalry
    ): Int = when (rivalry.intensity) {
        RivalryIntensity.LOW -> 0
        RivalryIntensity.MODERATE -> 3
        RivalryIntensity.HIGH -> 6
        RivalryIntensity.FIERCE -> 10
        RivalryIntensity.HISTORIC -> 14
    }

    fun attendanceMultiplier(
        rivalry: ClubRivalry
    ): Double = when (rivalry.intensity) {
        RivalryIntensity.LOW -> 1.00
        RivalryIntensity.MODERATE -> 1.05
        RivalryIntensity.HIGH -> 1.12
        RivalryIntensity.FIERCE -> 1.20
        RivalryIntensity.HISTORIC -> 1.30
    }

    private fun calculateScoreIncrease(
        input: RivalryMatchInput
    ): Int {
        var increase = 2

        if (input.isKnockout) increase += 2
        if (input.isFinal) increase += 8
        if (input.decidedTitle) increase += 12

        val goalDifference =
            abs(input.clubAGoals - input.clubBGoals)

        if (goalDifference >= 4) {
            increase += 3
        }

        if (input.attendance >= 50000) {
            increase += 2
        }

        return increase
    }

    private fun normalizeInput(
        rivalry: ClubRivalry,
        input: RivalryMatchInput
    ): RivalryMatchInput {
        if (input.clubAId == rivalry.clubAId) {
            return input
        }

        return input.copy(
            clubAId = input.clubBId,
            clubAName = input.clubBName,
            clubBId = input.clubAId,
            clubBName = input.clubAName,
            clubAGoals = input.clubBGoals,
            clubBGoals = input.clubAGoals
        )
    }

    private fun validateMatch(
        rivalry: ClubRivalry,
        input: RivalryMatchInput
    ) {
        require(input.clubAGoals >= 0)
        require(input.clubBGoals >= 0)
        require(input.attendance >= 0)

        val rivalryClubs =
            setOf(rivalry.clubAId, rivalry.clubBId)
        val matchClubs =
            setOf(input.clubAId, input.clubBId)

        require(rivalryClubs == matchClubs) {
            "A partida não pertence a esta rivalidade."
        }
    }
}
