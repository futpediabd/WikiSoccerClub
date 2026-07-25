package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionSeasonSnapshot
import com.wikisoccerclub.data.competition.model.GlobalCompetitionIntegrationResult

object CompetitionSeasonSnapshotEngine {

    fun create(
        result: GlobalCompetitionIntegrationResult
    ): CompetitionSeasonSnapshot =
        CompetitionSeasonSnapshot(
            season = result.season,
            competitionIds = result.competitions
                .map { it.competitionId },
            participantsByCompetition = result.competitions
                .associate { competition ->
                    competition.competitionId to competition.participants
                },
            matchDaysByCompetition = result.competitions
                .associate { competition ->
                    competition.competitionId to competition.calendar
                        .map { it.day }
                        .sorted()
                },
            warnings = result.warnings
        )
}
