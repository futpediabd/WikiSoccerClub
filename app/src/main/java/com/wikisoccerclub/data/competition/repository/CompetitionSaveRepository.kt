package com.wikisoccerclub.data.competition.repository

import com.wikisoccerclub.data.competition.model.CompetitionSaveState

interface CompetitionSaveRepository {
    fun save(state: CompetitionSaveState)
    fun load(competitionId: String, season: Int): CompetitionSaveState?
    fun delete(competitionId: String, season: Int)
}
