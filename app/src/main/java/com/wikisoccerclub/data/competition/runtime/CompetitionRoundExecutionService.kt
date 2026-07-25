package com.wikisoccerclub.data.competition.runtime

class CompetitionRoundExecutionService(
    private val repository: CompetitionRuntimeRepository
) {

    fun register(state: RuntimeCompetitionState) {
        require(state.matches.all {
            it.competitionId == state.competitionId &&
                it.season == state.season
        }) {
            "Há partidas vinculadas a outra competição ou temporada."
        }

        repository.save(state)
    }

    fun matchesForDay(
        competitionId: String,
        season: Int,
        day: Int
    ): List<RuntimeMatch> =
        requireState(competitionId, season)
            .matches
            .filter { it.day == day }
            .sortedWith(
                compareBy<RuntimeMatch> { it.round }
                    .thenBy { it.matchId }
            )

    fun executeDay(
        competitionId: String,
        season: Int,
        day: Int,
        results: List<RuntimeMatchResult>
    ): RuntimeRoundExecutionResult {
        val state = requireState(competitionId, season)
        val resultMap = results.associateBy { it.matchId }

        val updatedMatches = state.matches.map { match ->
            if (
                match.day == day &&
                match.status != RuntimeMatchStatus.FINISHED
            ) {
                val result = requireNotNull(resultMap[match.matchId]) {
                    "Resultado ausente para a partida ${match.matchId}."
                }

                CompetitionRuntimeEngine.finishMatch(
                    CompetitionRuntimeEngine.startMatch(match),
                    result
                )
            } else {
                match
            }
        }

        val updatedState = state.copy(
            currentDay = maxOf(state.currentDay, day),
            matches = updatedMatches
        )

        repository.save(updatedState)

        val finishedToday = updatedMatches.filter {
            it.day == day &&
                it.status == RuntimeMatchStatus.FINISHED
        }

        return RuntimeRoundExecutionResult(
            competitionId = competitionId,
            season = season,
            day = day,
            finishedMatches = finishedToday,
            remainingMatches = updatedMatches.count {
                it.status != RuntimeMatchStatus.FINISHED
            }
        )
    }

    fun nextPendingDay(
        competitionId: String,
        season: Int
    ): Int? =
        requireState(competitionId, season)
            .matches
            .filter {
                it.status != RuntimeMatchStatus.FINISHED
            }
            .minOfOrNull { it.day }

    private fun requireState(
        competitionId: String,
        season: Int
    ): RuntimeCompetitionState =
        requireNotNull(repository.find(competitionId, season)) {
            "Competição não registrada no motor de execução."
        }
}
