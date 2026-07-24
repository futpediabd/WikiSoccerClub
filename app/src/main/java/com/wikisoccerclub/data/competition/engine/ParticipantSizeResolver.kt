package com.wikisoccerclub.data.competition.engine

object ParticipantSizeResolver {
    fun largestAllowed(availableClubs: Int, allowedSizes: List<Int>, minimum: Int): Int? {
        if (availableClubs < minimum) return null
        return allowedSizes.sortedDescending().firstOrNull { availableClubs >= it }
    }

    fun nationalCupSize(availableClubs: Int): Int? =
        largestAllowed(availableClubs, listOf(256,128,64,32,16), 16)

    fun worldCupSize(availableClubs: Int): Int? =
        largestAllowed(availableClubs, listOf(256,128,64,32), 32)
}
