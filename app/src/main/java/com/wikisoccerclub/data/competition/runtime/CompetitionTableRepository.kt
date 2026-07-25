package com.wikisoccerclub.data.competition.runtime

interface CompetitionTableRepository {
    fun save(table: CompetitionTable)
    fun find(competitionId: String, season: Int): CompetitionTable?
}

class InMemoryCompetitionTableRepository :
    CompetitionTableRepository {

    private val tables =
        mutableMapOf<Pair<String, Int>, CompetitionTable>()

    override fun save(table: CompetitionTable) {
        tables[table.competitionId to table.season] = table
    }

    override fun find(
        competitionId: String,
        season: Int
    ): CompetitionTable? =
        tables[competitionId to season]
}
