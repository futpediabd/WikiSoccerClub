package com.wikisoccerclub.data.morale

class MoraleRepository {

    private val playerMorales =
        linkedMapOf<String, PlayerMorale>()

    private val concerns =
        linkedMapOf<String, PlayerConcern>()

    private val atmospheres =
        linkedMapOf<String, SquadAtmosphere>()

    fun saveMorale(morale: PlayerMorale) {
        playerMorales[morale.playerId] = morale
    }

    fun saveMorales(values: List<PlayerMorale>) {
        values.forEach(::saveMorale)
    }

    fun findMorale(playerId: String):
        PlayerMorale? = playerMorales[playerId]

    fun allMorales(): List<PlayerMorale> =
        playerMorales.values.toList()

    fun saveConcern(concern: PlayerConcern) {
        concerns[concern.id] = concern
    }

    fun findConcern(id: String):
        PlayerConcern? = concerns[id]

    fun activeConcerns(): List<PlayerConcern> =
        concerns.values.filter {
            it.status == ConcernStatus.ACTIVE
        }

    fun concernsByPlayer(
        playerId: String
    ): List<PlayerConcern> =
        concerns.values.filter {
            it.playerId == playerId
        }

    fun saveAtmosphere(
        atmosphere: SquadAtmosphere
    ) {
        atmospheres[atmosphere.clubId] =
            atmosphere
    }

    fun findAtmosphere(
        clubId: String
    ): SquadAtmosphere? =
        atmospheres[clubId]
}
