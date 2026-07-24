package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.SeasonTransitionResult

interface SeasonTransitionRepository {
    fun save(result: SeasonTransitionResult)
    fun load(seasonFinished: Int): SeasonTransitionResult?
}
