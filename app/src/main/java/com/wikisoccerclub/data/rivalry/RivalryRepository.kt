package com.wikisoccerclub.data.rivalry

class RivalryRepository {

    private val rivalries =
        linkedMapOf<String, ClubRivalry>()

    private val matches =
        linkedMapOf<String, RivalryMatchInput>()

    fun saveRivalry(rivalry: ClubRivalry) {
        rivalries[rivalry.id] = rivalry
    }

    fun findRivalry(
        clubAId: String,
        clubBId: String
    ): ClubRivalry? =
        rivalries[
            RivalryEngine.rivalryId(
                clubAId,
                clubBId
            )
        ]

    fun rivalriesByClub(
        clubId: String
    ): List<ClubRivalry> =
        rivalries.values
            .filter {
                it.clubAId == clubId ||
                    it.clubBId == clubId
            }
            .sortedByDescending { it.score }

    fun allRivalries(): List<ClubRivalry> =
        rivalries.values
            .sortedByDescending { it.score }

    fun saveMatch(input: RivalryMatchInput) {
        matches[input.matchId] = input
    }

    fun matchesFor(
        rivalry: ClubRivalry
    ): List<RivalryMatchInput> =
        matches.values.filter {
            setOf(it.clubAId, it.clubBId) ==
                setOf(
                    rivalry.clubAId,
                    rivalry.clubBId
                )
        }

    fun allMatches():
        List<RivalryMatchInput> =
        matches.values.toList()
}
