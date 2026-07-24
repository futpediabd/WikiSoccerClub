package com.wikisoccerclub.data.youth

class YouthRepository {

    private val tryouts =
        linkedMapOf<String, YouthTryoutResult>()

    fun saveTryout(
        result: YouthTryoutResult
    ) {
        tryouts[result.id] = result
    }

    fun findTryout(
        tryoutId: String
    ): YouthTryoutResult? =
        tryouts[tryoutId]

    fun latestTryout(
        clubId: String
    ): YouthTryoutResult? =
        tryouts.values
            .filter {
                it.clubId == clubId
            }
            .maxByOrNull {
                it.generatedDay
            }

    fun tryoutsByClub(
        clubId: String
    ): List<YouthTryoutResult> =
        tryouts.values
            .filter {
                it.clubId == clubId
            }
            .sortedByDescending {
                it.generatedDay
            }

    fun updateCandidate(
        tryoutId: String,
        candidate: YouthCandidate
    ) {
        val result =
            tryouts[tryoutId] ?: return

        tryouts[tryoutId] =
            result.copy(
                candidates =
                    result.candidates.map {
                        if (it.id == candidate.id) {
                            candidate
                        } else {
                            it
                        }
                    }
            )
    }
}
