package com.wikisoccerclub.data.competition

/**
 * Classificações compartilhadas da carreira, separadas por competição.
 * O conteúdo pode ser incluído no save da carreira sem depender da interface.
 */
class CareerStandingsRepository(
    initial: List<CompetitionStandings> = emptyList()
) {
    private val standings = linkedMapOf<String, CompetitionStandings>()

    init { initial.forEach(::save) }

    fun save(value: CompetitionStandings): CompetitionStandings {
        val normalized = value.copy(entries = StandingsEngine.sort(value.entries))
        standings[value.competitionId] = normalized
        return normalized
    }

    fun find(competitionId: String): CompetitionStandings? = standings[competitionId]

    fun getOrCreate(
        competitionId: String,
        teams: Map<String, String> = emptyMap()
    ): CompetitionStandings {
        val current = standings[competitionId]
        if (current != null) return current

        return CompetitionStandings(
            competitionId = competitionId,
            entries = teams.map { (id, name) -> StandingEntry(teamId = id, teamName = name) }
        ).let(::save)
    }

    fun all(): List<CompetitionStandings> = standings.values.toList()

    fun clearCompetition(competitionId: String) {
        standings.remove(competitionId)
    }

    fun clear() = standings.clear()
}
