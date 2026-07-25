package com.wikisoccerclub.data.competition.runtime

data class RoundAndTableResult(
    val roundExecution: RuntimeRoundExecutionResult,
    val tableUpdate: CompetitionTableUpdateResult
)

class CompetitionRoundAndTableCoordinator(
    private val roundExecutionService: CompetitionRoundExecutionService,
    private val tableService: CompetitionTableService
) {

    fun executeDayAndUpdateTable(
        competitionId: String,
        season: Int,
        day: Int,
        results: List<RuntimeMatchResult>
    ): RoundAndTableResult {
        val roundResult = roundExecutionService.executeDay(
            competitionId = competitionId,
            season = season,
            day = day,
            results = results
        )

        val tableResult = tableService.updateFromRound(
            competitionId = competitionId,
            season = season,
            finishedMatches = roundResult.finishedMatches
        )

        return RoundAndTableResult(
            roundExecution = roundResult,
            tableUpdate = tableResult
        )
    }
}
