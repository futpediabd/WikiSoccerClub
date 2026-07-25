package com.wikisoccerclub.data.competition.runtime

class CompetitionTableService(
    private val repository: CompetitionTableRepository
) {

    fun initialize(
        competitionId: String,
        season: Int,
        clubIds: List<String>
    ): CompetitionTable {
        require(clubIds.distinct().size >= 2) {
            "A competição precisa de pelo menos dois clubes."
        }

        val table = CompetitionTableEngine.buildInitialTable(
            competitionId = competitionId,
            season = season,
            clubIds = clubIds
        )

        repository.save(table)
        return table
    }

    fun updateFromRound(
        competitionId: String,
        season: Int,
        finishedMatches: List<RuntimeMatch>
    ): CompetitionTableUpdateResult {
        val current = requireNotNull(
            repository.find(competitionId, season)
        ) {
            "Tabela da competição ainda não foi inicializada."
        }

        val updated = CompetitionTableEngine.applyMatches(
            table = current,
            matches = finishedMatches
        )

        repository.save(updated)

        return CompetitionTableUpdateResult(
            competitionId = competitionId,
            season = season,
            updatedTable = updated,
            processedMatches = finishedMatches.count {
                it.status == RuntimeMatchStatus.FINISHED
            }
        )
    }

    fun getTable(
        competitionId: String,
        season: Int
    ): CompetitionTable =
        requireNotNull(repository.find(competitionId, season)) {
            "Tabela não encontrada."
        }
}
