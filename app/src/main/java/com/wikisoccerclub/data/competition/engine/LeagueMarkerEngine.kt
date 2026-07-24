package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.LeagueStandingEntry
import com.wikisoccerclub.data.competition.model.QualificationMarker

object LeagueMarkerEngine {
    fun applyMarkers(
        table: List<LeagueStandingEntry>,
        markers: List<QualificationMarker>
    ): List<LeagueStandingEntry> =
        table.map { entry ->
            entry.copy(
                markers = markers.filter {
                    entry.position in it.fromPosition..it.toPosition
                }
            )
        }
}
