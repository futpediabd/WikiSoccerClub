package com.wikisoccerclub.data.season

class SeasonRepository(
    initialSeason: Season = Season(year = 2026)
) {
    private var season = initialSeason
    private val summaries = mutableListOf<SeasonSummary>()
    private val divisions =
        linkedMapOf<String, LeagueDivisionState>()

    fun current(): Season = season

    fun saveSeason(value: Season) {
        season = value
    }

    fun saveSummary(summary: SeasonSummary) {
        summaries.removeAll { it.year == summary.year }
        summaries += summary
    }

    fun history(): List<SeasonSummary> =
        summaries.sortedByDescending { it.year }

    fun saveDivisions(values: List<LeagueDivisionState>) {
        divisions.clear()
        values.forEach {
            divisions[it.competitionId] = it
        }
    }

    fun divisions(): List<LeagueDivisionState> =
        divisions.values.sortedBy { it.divisionLevel }
}
