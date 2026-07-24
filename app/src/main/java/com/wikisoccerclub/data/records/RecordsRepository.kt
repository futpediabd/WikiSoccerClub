package com.wikisoccerclub.data.records

class RecordsRepository {

    private val playerStats =
        linkedMapOf<String, PlayerCareerStats>()

    private val clubStats =
        linkedMapOf<String, ClubCareerStats>()

    private val records =
        linkedMapOf<String, FootballRecord>()

    private val hallOfFame =
        linkedMapOf<String, HallOfFameEntry>()

    private val awards =
        linkedMapOf<String, SeasonAward>()

    fun savePlayerStats(stats: PlayerCareerStats) {
        playerStats[stats.playerId] = stats
    }

    fun findPlayerStats(playerId: String):
        PlayerCareerStats? = playerStats[playerId]

    fun allPlayerStats():
        List<PlayerCareerStats> =
        playerStats.values.toList()

    fun saveClubStats(stats: ClubCareerStats) {
        clubStats[stats.clubId] = stats
    }

    fun findClubStats(clubId: String):
        ClubCareerStats? = clubStats[clubId]

    fun allClubStats():
        List<ClubCareerStats> =
        clubStats.values.toList()

    fun saveRecord(record: FootballRecord) {
        records[record.id] = record
    }

    fun saveRecords(values: List<FootballRecord>) {
        values.forEach(::saveRecord)
    }

    fun records(
        scope: RecordScope? = null,
        category: RecordCategory? = null
    ): List<FootballRecord> =
        records.values.filter {
            (scope == null || it.scope == scope) &&
                (category == null ||
                    it.category == category)
        }.sortedByDescending { it.value }

    fun saveHallOfFameEntry(
        entry: HallOfFameEntry
    ) {
        hallOfFame[entry.id] = entry
    }

    fun hallOfFameEntries():
        List<HallOfFameEntry> =
        hallOfFame.values.sortedByDescending {
            it.score
        }

    fun saveAward(award: SeasonAward) {
        awards[award.id] = award
    }

    fun awardsBySeason(
        seasonYear: Int
    ): List<SeasonAward> =
        awards.values.filter {
            it.seasonYear == seasonYear
        }
}
