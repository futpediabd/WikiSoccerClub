package com.wikisoccerclub.data.rivalry

object RivalrySummaryEngine {

    fun build(
        rivalry: ClubRivalry,
        matches: List<RivalryMatchInput>
    ): RivalrySummary {
        val relevant = matches
            .filter {
                setOf(it.clubAId, it.clubBId) ==
                    setOf(
                        rivalry.clubAId,
                        rivalry.clubBId
                    )
            }
            .map {
                normalize(rivalry, it)
            }
            .sortedByDescending {
                it.seasonYear
            }

        val biggestA = relevant
            .filter {
                it.clubAGoals > it.clubBGoals
            }
            .maxByOrNull {
                it.clubAGoals - it.clubBGoals
            }
            ?.let(::scoreText)

        val biggestB = relevant
            .filter {
                it.clubBGoals > it.clubAGoals
            }
            .maxByOrNull {
                it.clubBGoals - it.clubAGoals
            }
            ?.let(::scoreText)

        return RivalrySummary(
            rivalry = rivalry,
            biggestClubAWin = biggestA,
            biggestClubBWin = biggestB,
            highestAttendance =
                relevant.maxOfOrNull {
                    it.attendance
                } ?: 0,
            recentMatches = relevant.take(10)
        )
    }

    private fun normalize(
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

    private fun scoreText(
        match: RivalryMatchInput
    ): String =
        "${match.clubAName} ${match.clubAGoals} x " +
            "${match.clubBGoals} ${match.clubBName}"
}
