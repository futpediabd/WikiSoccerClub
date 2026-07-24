package com.wikisoccerclub.data.stadium

class StadiumRepository {

    private val stadiums =
        linkedMapOf<String, Stadium>()

    private val attendanceResults =
        linkedMapOf<String, MatchAttendanceResult>()

    private val upgrades =
        linkedMapOf<String, StadiumUpgrade>()

    fun saveStadium(stadium: Stadium) {
        stadiums[stadium.id] = stadium
    }

    fun findStadium(stadiumId: String): Stadium? =
        stadiums[stadiumId]

    fun findByClub(clubId: String): Stadium? =
        stadiums.values.firstOrNull {
            it.clubId == clubId
        }

    fun allStadiums(): List<Stadium> =
        stadiums.values.toList()

    fun saveAttendance(
        result: MatchAttendanceResult
    ) {
        attendanceResults[result.matchId] = result
    }

    fun findAttendance(
        matchId: String
    ): MatchAttendanceResult? =
        attendanceResults[matchId]

    fun attendanceHistory():
        List<MatchAttendanceResult> =
        attendanceResults.values.toList()

    fun saveUpgrade(upgrade: StadiumUpgrade) {
        upgrades[upgrade.id] = upgrade
    }

    fun activeUpgrades(
        stadiumId: String
    ): List<StadiumUpgrade> =
        upgrades.values.filter {
            it.stadiumId == stadiumId &&
                !it.completed
        }

    fun markUpgradeCompleted(upgradeId: String) {
        val upgrade = upgrades[upgradeId] ?: return
        upgrades[upgradeId] =
            upgrade.copy(completed = true)
    }

    fun findUpgrade(
        upgradeId: String
    ): StadiumUpgrade? = upgrades[upgradeId]
}
