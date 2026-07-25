package com.wikisoccerclub.data.competition.runtime

object CompetitionTableEngine {

    fun buildInitialTable(
        competitionId: String,
        season: Int,
        clubIds: List<String>
    ): CompetitionTable {
        val entries = clubIds
            .distinct()
            .map { CompetitionTableEntry(clubId = it) }

        return CompetitionTable(
            competitionId = competitionId,
            season = season,
            entries = entries
        )
    }

    fun applyFinishedMatch(
        table: CompetitionTable,
        match: RuntimeMatch
    ): CompetitionTable {
        require(match.status == RuntimeMatchStatus.FINISHED) {
            "Somente partidas encerradas podem atualizar a tabela."
        }

        val homeGoals = requireNotNull(match.homeGoals)
        val awayGoals = requireNotNull(match.awayGoals)

        require(table.entries.any { it.clubId == match.homeClubId }) {
            "Mandante não encontrado na tabela."
        }

        require(table.entries.any { it.clubId == match.awayClubId }) {
            "Visitante não encontrado na tabela."
        }

        val updatedEntries = table.entries.map { entry ->
            when (entry.clubId) {
                match.homeClubId -> updateEntry(
                    entry = entry,
                    goalsFor = homeGoals,
                    goalsAgainst = awayGoals
                )

                match.awayClubId -> updateEntry(
                    entry = entry,
                    goalsFor = awayGoals,
                    goalsAgainst = homeGoals
                )

                else -> entry
            }
        }

        return table.copy(entries = sortEntries(updatedEntries))
    }

    fun applyMatches(
        table: CompetitionTable,
        matches: List<RuntimeMatch>
    ): CompetitionTable =
        matches
            .filter { it.status == RuntimeMatchStatus.FINISHED }
            .fold(table) { current, match ->
                applyFinishedMatch(current, match)
            }

    fun sortEntries(
        entries: List<CompetitionTableEntry>
    ): List<CompetitionTableEntry> =
        entries.sortedWith(
            compareByDescending<CompetitionTableEntry> { it.points }
                .thenByDescending { it.wins }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
                .thenBy { it.clubId }
        )

    private fun updateEntry(
        entry: CompetitionTableEntry,
        goalsFor: Int,
        goalsAgainst: Int
    ): CompetitionTableEntry {
        val win = goalsFor > goalsAgainst
        val draw = goalsFor == goalsAgainst
        val loss = goalsFor < goalsAgainst

        return entry.copy(
            played = entry.played + 1,
            wins = entry.wins + if (win) 1 else 0,
            draws = entry.draws + if (draw) 1 else 0,
            losses = entry.losses + if (loss) 1 else 0,
            goalsFor = entry.goalsFor + goalsFor,
            goalsAgainst = entry.goalsAgainst + goalsAgainst,
            points = entry.points + when {
                win -> 3
                draw -> 1
                else -> 0
            }
        )
    }
}
