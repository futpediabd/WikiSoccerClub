package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.CompetitionSaveState

class InMemoryCompetitionSaveRepository : CompetitionSaveRepository {

    private val states = linkedMapOf<String, CompetitionSaveState>()

    override fun save(state: CompetitionSaveState) {
        states[key(state.competitionId, state.season)] = state
    }

    override fun load(
        competitionId: String,
        season: Int
    ): CompetitionSaveState? =
        states[key(competitionId, season)]

    override fun delete(
        competitionId: String,
        season: Int
    ) {
        states.remove(key(competitionId, season))
    }

    private fun key(competitionId: String, season: Int): String =
        "$competitionId::$season"
}
