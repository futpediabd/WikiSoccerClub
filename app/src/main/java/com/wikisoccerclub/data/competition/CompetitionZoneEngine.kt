package com.wikisoccerclub.data.competition

object CompetitionZoneEngine {

    fun apply(
        standings: CompetitionStandings,
        rules: List<CompetitionZoneRule>
    ): List<StandingWithZone> =
        standings.entries.mapIndexed { index, entry ->
            val position = index + 1
            StandingWithZone(
                position = position,
                entry = entry,
                zone = rules.firstOrNull {
                    position in it.startPosition..it.endPosition
                }
            )
        }

    fun brazilianSerieARules(): List<CompetitionZoneRule> =
        listOf(
            CompetitionZoneRule(
                startPosition = 1,
                endPosition = 1,
                type = CompetitionZoneType.CHAMPION,
                label = "Campeão"
            ),
            CompetitionZoneRule(
                startPosition = 2,
                endPosition = 6,
                type = CompetitionZoneType.CONTINENTAL,
                label = "Classificação continental"
            ),
            CompetitionZoneRule(
                startPosition = 17,
                endPosition = 20,
                type = CompetitionZoneType.RELEGATION,
                label = "Rebaixamento"
            )
        )

    fun brazilianSerieBRules(): List<CompetitionZoneRule> =
        listOf(
            CompetitionZoneRule(
                startPosition = 1,
                endPosition = 4,
                type = CompetitionZoneType.PROMOTION,
                label = "Acesso"
            ),
            CompetitionZoneRule(
                startPosition = 17,
                endPosition = 20,
                type = CompetitionZoneType.RELEGATION,
                label = "Rebaixamento"
            )
        )
}
