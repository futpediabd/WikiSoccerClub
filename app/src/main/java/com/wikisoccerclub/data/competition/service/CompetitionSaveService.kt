package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.model.CompetitionSaveState
import com.wikisoccerclub.data.competition.repository.CompetitionSaveRepository

class CompetitionSaveService(
    private val repository: CompetitionSaveRepository
) {
    fun save(state: CompetitionSaveState) {
        repository.save(state)
    }

    fun load(
        competitionId: String,
        season: Int
    ): CompetitionSaveState? =
        repository.load(competitionId, season)

    fun remove(
        competitionId: String,
        season: Int
    ) {
        repository.delete(competitionId, season)
    }
}
