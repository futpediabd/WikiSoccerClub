package com.wikisoccerclub.data.headtohead

/** Histórico de confrontos sem duplicar partidas já registradas. */
class HeadToHeadRepository(initialMatches: List<HeadToHeadMatch> = emptyList()) {
    private val matches = linkedMapOf<String, HeadToHeadMatch>()

    init { saveAll(initialMatches) }

    fun save(match: HeadToHeadMatch): HeadToHeadMatch {
        require(match.homeGoals >= 0 && match.awayGoals >= 0) { "O placar não pode ser negativo." }
        matches[match.matchId] = match
        return match
    }

    fun saveAll(items: List<HeadToHeadMatch>) = items.forEach(::save)

    fun all(): List<HeadToHeadMatch> = matches.values.sortedWith(
        compareByDescending<HeadToHeadMatch> { it.date }.thenByDescending { it.matchId }
    )

    fun between(firstClubId: String, secondClubId: String): List<HeadToHeadMatch> = all().filter {
        (it.homeClubId == firstClubId && it.awayClubId == secondClubId) ||
            (it.homeClubId == secondClubId && it.awayClubId == firstClubId)
    }

    fun clear() = matches.clear()
}
