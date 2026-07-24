package com.wikisoccerclub.data.match

class MatchResultRepository {

    private val results = linkedMapOf<String, CompletedMatchResult>()

    fun save(result: CompletedMatchResult) {
        results[result.matchId] = result
    }

    fun findByMatchId(matchId: String): CompletedMatchResult? =
        results[matchId]

    fun all(): List<CompletedMatchResult> =
        results.values.toList()

    fun clear() {
        results.clear()
    }
}
