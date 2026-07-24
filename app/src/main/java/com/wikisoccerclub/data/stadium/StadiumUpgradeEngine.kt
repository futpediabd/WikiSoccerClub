package com.wikisoccerclub.data.stadium

object StadiumUpgradeEngine {

    fun createUpgrade(
        stadium: Stadium,
        facility: StadiumFacility,
        targetLevel: Int,
        currentDay: Int
    ): StadiumUpgrade {
        require(targetLevel in 1..100)

        val currentLevel =
            levelFor(stadium, facility)

        require(targetLevel > currentLevel) {
            "O nível desejado deve ser superior ao atual."
        }

        val difference =
            targetLevel - currentLevel

        val baseCost = when (facility) {
            StadiumFacility.SEATING -> 180_000L
            StadiumFacility.PITCH -> 90_000L
            StadiumFacility.LIGHTING -> 110_000L
            StadiumFacility.DRAINAGE -> 100_000L
            StadiumFacility.SECURITY -> 80_000L
            StadiumFacility.HOSPITALITY -> 130_000L
            StadiumFacility.PARKING -> 70_000L
        }

        val durationPerLevel = when (facility) {
            StadiumFacility.SEATING -> 3
            else -> 2
        }

        return StadiumUpgrade(
            id =
                "obra_${stadium.id}_${facility.name}_$currentDay",
            stadiumId = stadium.id,
            facility = facility,
            currentLevel = currentLevel,
            targetLevel = targetLevel,
            cost = difference * baseCost,
            durationDays =
                difference * durationPerLevel,
            startedDay = currentDay
        )
    }

    fun canComplete(
        upgrade: StadiumUpgrade,
        currentDay: Int
    ): Boolean =
        !upgrade.completed &&
            currentDay >=
                upgrade.startedDay +
                    upgrade.durationDays

    fun complete(
        stadium: Stadium,
        upgrade: StadiumUpgrade,
        currentDay: Int
    ): Stadium {
        require(stadium.id == upgrade.stadiumId)
        require(canComplete(upgrade, currentDay)) {
            "A obra ainda não foi concluída."
        }

        return when (upgrade.facility) {
            StadiumFacility.SEATING ->
                stadium.copy(
                    capacity =
                        stadium.capacity +
                            (upgrade.targetLevel -
                                upgrade.currentLevel) *
                            1_000
                )
            StadiumFacility.PITCH ->
                stadium.copy(
                    pitchQuality =
                        upgrade.targetLevel
                )
            StadiumFacility.LIGHTING ->
                stadium.copy(
                    lightingQuality =
                        upgrade.targetLevel
                )
            StadiumFacility.DRAINAGE ->
                stadium.copy(
                    drainageQuality =
                        upgrade.targetLevel
                )
            StadiumFacility.SECURITY ->
                stadium.copy(
                    securityQuality =
                        upgrade.targetLevel
                )
            StadiumFacility.HOSPITALITY ->
                stadium.copy(
                    hospitalityQuality =
                        upgrade.targetLevel
                )
            StadiumFacility.PARKING ->
                stadium.copy(
                    parkingQuality =
                        upgrade.targetLevel
                )
        }
    }

    fun levelFor(
        stadium: Stadium,
        facility: StadiumFacility
    ): Int = when (facility) {
        StadiumFacility.SEATING ->
            (stadium.capacity / 1_000)
                .coerceIn(1, 100)
        StadiumFacility.PITCH ->
            stadium.pitchQuality
        StadiumFacility.LIGHTING ->
            stadium.lightingQuality
        StadiumFacility.DRAINAGE ->
            stadium.drainageQuality
        StadiumFacility.SECURITY ->
            stadium.securityQuality
        StadiumFacility.HOSPITALITY ->
            stadium.hospitalityQuality
        StadiumFacility.PARKING ->
            stadium.parkingQuality
    }
}
