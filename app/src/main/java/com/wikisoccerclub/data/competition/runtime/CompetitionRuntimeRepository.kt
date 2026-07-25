package com.wikisoccerclub.data.competition.runtime

interface CompetitionRuntimeRepository {
    fun save(state: RuntimeCompetitionState)
    fun find(competitionId: String, season: Int): RuntimeCompetitionState?
}

class InMemoryCompetitionRuntimeRepository :
    CompetitionRuntimeRepository {

    private val states =
        mutableMapOf<Pair<String, Int>, RuntimeCompetitionState>()

    override fun save(state: RuntimeCompetitionState) {
        states[state.competitionId to state.season] = state
    }

    override fun find(
        competitionId: String,
        season: Int
    ): RuntimeCompetitionState? =
        states[competitionId to season]
}
