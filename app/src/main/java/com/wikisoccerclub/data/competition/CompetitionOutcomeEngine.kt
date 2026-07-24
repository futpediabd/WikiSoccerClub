package com.wikisoccerclub.data.competition

object CompetitionOutcomeEngine {

    fun calculate(
        progress: CompetitionProgress,
        zoneRules: List<CompetitionZoneRule>
    ): CompetitionOutcome {
        val allMatchesPlayed = progress.matches.isNotEmpty() &&
            progress.matches.all { it.played }

        if (!allMatchesPlayed) {
            return CompetitionOutcome(
                competitionId = progress.competitionId,
                championTeamId = null,
                completed = false
            )
        }

        val zoned = CompetitionZoneEngine.apply(
            standings = progress.standings,
            rules = zoneRules
        )

        return CompetitionOutcome(
            competitionId = progress.competitionId,
            championTeamId = zoned.firstOrNull()?.entry?.teamId,
            promotedTeamIds = zoned.filter {
                it.zone?.type == CompetitionZoneType.PROMOTION
            }.map { it.entry.teamId },
            relegatedTeamIds = zoned.filter {
                it.zone?.type == CompetitionZoneType.RELEGATION
            }.map { it.entry.teamId },
            continentalQualifiedTeamIds = zoned.filter {
                it.zone?.type == CompetitionZoneType.CONTINENTAL
            }.map { it.entry.teamId },
            completed = true
        )
    }
}
