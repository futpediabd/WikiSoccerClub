package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.SeasonTransitionResult

class InMemorySeasonTransitionRepository : SeasonTransitionRepository {

    private val transitions = linkedMapOf<Int, SeasonTransitionResult>()

    override fun save(result: SeasonTransitionResult) {
        transitions[result.seasonFinished] = result
    }

    override fun load(seasonFinished: Int): SeasonTransitionResult? =
        transitions[seasonFinished]
}
