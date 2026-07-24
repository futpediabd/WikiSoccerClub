package com.wikisoccerclub.data.morale

object SquadAtmosphereEngine {

    fun calculate(
        clubId: String,
        playerMorales: List<PlayerMorale>,
        captainLeadership: Int,
        recentResultsScore: Int
    ): SquadAtmosphere {
        require(captainLeadership in 0..100)
        require(recentResultsScore in 0..100)

        if (playerMorales.isEmpty()) {
            return SquadAtmosphere(
                clubId = clubId,
                atmosphere = 50,
                leadership = captainLeadership,
                cohesion = 50,
                managerSupport = 50,
                unhappyPlayers = 0
            )
        }

        val averageMorale =
            playerMorales.map { it.morale }.average()
        val cohesion =
            playerMorales.map {
                it.squadRelationship
            }.average().toInt()
        val managerSupport =
            playerMorales.map {
                it.managerRelationship
            }.average().toInt()
        val unhappy = playerMorales.count {
            it.morale < 30 || it.happiness < 30
        }

        val unhappyPenalty =
            (unhappy * 4).coerceAtMost(30)

        val atmosphere = (
            averageMorale * 0.35 +
                cohesion * 0.25 +
                managerSupport * 0.20 +
                captainLeadership * 0.10 +
                recentResultsScore * 0.10 -
                unhappyPenalty
            ).toInt().coerceIn(0, 100)

        return SquadAtmosphere(
            clubId = clubId,
            atmosphere = atmosphere,
            leadership = captainLeadership,
            cohesion = cohesion,
            managerSupport = managerSupport,
            unhappyPlayers = unhappy
        )
    }

    fun dressingRoomScore(
        atmosphere: SquadAtmosphere
    ): Int = (
        atmosphere.atmosphere * 0.55 +
            atmosphere.cohesion * 0.25 +
            atmosphere.managerSupport * 0.20
        ).toInt().coerceIn(0, 100)
}
