package com.wikisoccerclub.data.records

object RecordEngine {

    fun playerRecords(
        stats: List<PlayerCareerStats>,
        scope: RecordScope,
        scopeId: String?
    ): List<FootballRecord> {
        if (stats.isEmpty()) return emptyList()

        return listOfNotNull(
            maximumPlayerRecord(
                stats,
                RecordCategory.MOST_MATCHES,
                scope,
                scopeId,
                "Maior número de partidas"
            ) { it.matches.toLong() },
            maximumPlayerRecord(
                stats,
                RecordCategory.MOST_GOALS,
                scope,
                scopeId,
                "Maior número de gols"
            ) { it.goals.toLong() },
            maximumPlayerRecord(
                stats,
                RecordCategory.MOST_ASSISTS,
                scope,
                scopeId,
                "Maior número de assistências"
            ) { it.assists.toLong() },
            maximumPlayerRecord(
                stats,
                RecordCategory.MOST_CLEAN_SHEETS,
                scope,
                scopeId,
                "Maior número de jogos sem sofrer gols"
            ) { it.cleanSheets.toLong() },
            maximumPlayerRecord(
                stats,
                RecordCategory.MOST_TITLES,
                scope,
                scopeId,
                "Maior número de títulos"
            ) { it.titles.toLong() }
        )
    }

    fun clubRecords(
        stats: List<ClubCareerStats>,
        scope: RecordScope,
        scopeId: String?
    ): List<FootballRecord> {
        if (stats.isEmpty()) return emptyList()

        return listOfNotNull(
            maximumClubRecord(
                stats,
                RecordCategory.MOST_MATCHES,
                scope,
                scopeId,
                "Clube com mais partidas"
            ) { it.matches.toLong() },
            maximumClubRecord(
                stats,
                RecordCategory.MOST_WINS,
                scope,
                scopeId,
                "Clube com mais vitórias"
            ) { it.wins.toLong() },
            maximumClubRecord(
                stats,
                RecordCategory.MOST_GOALS,
                scope,
                scopeId,
                "Clube com mais gols"
            ) { it.goalsFor.toLong() },
            maximumClubRecord(
                stats,
                RecordCategory.MOST_TITLES,
                scope,
                scopeId,
                "Clube com mais títulos"
            ) { it.titles.toLong() },
            maximumClubRecord(
                stats,
                RecordCategory.HIGHEST_ATTENDANCE,
                scope,
                scopeId,
                "Maior público"
            ) { it.highestAttendance.toLong() },
            maximumClubRecord(
                stats,
                RecordCategory.HIGHEST_TRANSFER_FEE,
                scope,
                scopeId,
                "Maior transferência"
            ) { it.biggestTransferFee }
        )
    }

    fun matchRecords(
        matches: List<MatchRecordInput>,
        scope: RecordScope,
        scopeId: String?
    ): List<FootballRecord> {
        if (matches.isEmpty()) return emptyList()

        val biggestWin = matches.maxByOrNull {
            kotlin.math.abs(it.homeGoals - it.awayGoals)
        }

        val highestAttendance = matches.maxByOrNull {
            it.attendance
        }

        return buildList {
            biggestWin?.let {
                val homeWon = it.homeGoals >= it.awayGoals
                val winnerId =
                    if (homeWon) it.homeClubId else it.awayClubId
                val winnerName =
                    if (homeWon) it.homeClubName else it.awayClubName
                val margin =
                    kotlin.math.abs(
                        it.homeGoals - it.awayGoals
                    )

                add(
                    FootballRecord(
                        id = "record_biggest_${it.matchId}",
                        category =
                            RecordCategory.BIGGEST_WIN,
                        scope = scope,
                        scopeId = scopeId,
                        holderId = winnerId,
                        holderName = winnerName,
                        value = margin.toLong(),
                        secondaryValue =
                            maxOf(it.homeGoals, it.awayGoals)
                                .toLong(),
                        seasonYear = it.seasonYear,
                        description =
                            "${it.homeClubName} ${it.homeGoals} x " +
                                "${it.awayGoals} ${it.awayClubName}"
                    )
                )
            }

            highestAttendance?.let {
                add(
                    FootballRecord(
                        id = "record_att_${it.matchId}",
                        category =
                            RecordCategory.HIGHEST_ATTENDANCE,
                        scope = scope,
                        scopeId = scopeId,
                        holderId = it.homeClubId,
                        holderName = it.homeClubName,
                        value = it.attendance.toLong(),
                        seasonYear = it.seasonYear,
                        description =
                            "Maior público registrado em uma partida"
                    )
                )
            }
        }
    }

    private fun maximumPlayerRecord(
        stats: List<PlayerCareerStats>,
        category: RecordCategory,
        scope: RecordScope,
        scopeId: String?,
        description: String,
        selector: (PlayerCareerStats) -> Long
    ): FootballRecord? {
        val holder = stats.maxByOrNull(selector) ?: return null
        return FootballRecord(
            id = "player_${category.name}_${holder.playerId}",
            category = category,
            scope = scope,
            scopeId = scopeId,
            holderId = holder.playerId,
            holderName = holder.playerName,
            value = selector(holder),
            seasonYear = null,
            description = description
        )
    }

    private fun maximumClubRecord(
        stats: List<ClubCareerStats>,
        category: RecordCategory,
        scope: RecordScope,
        scopeId: String?,
        description: String,
        selector: (ClubCareerStats) -> Long
    ): FootballRecord? {
        val holder = stats.maxByOrNull(selector) ?: return null
        return FootballRecord(
            id = "club_${category.name}_${holder.clubId}",
            category = category,
            scope = scope,
            scopeId = scopeId,
            holderId = holder.clubId,
            holderName = holder.clubName,
            value = selector(holder),
            seasonYear = null,
            description = description
        )
    }
}
