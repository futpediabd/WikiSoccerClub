package com.wikisoccerclub.data.medical

class MedicalRepository {

    private val profiles =
        linkedMapOf<String, PlayerMedicalProfile>()

    private val injuries =
        linkedMapOf<String, Injury>()

    private val disciplines =
        linkedMapOf<String, PlayerDiscipline>()

    private val suspensions =
        linkedMapOf<String, Suspension>()

    fun saveProfile(profile: PlayerMedicalProfile) {
        profiles[profile.playerId] = profile
    }

    fun findProfile(playerId: String):
        PlayerMedicalProfile? = profiles[playerId]

    fun saveInjury(injury: Injury) {
        injuries[injury.id] = injury
    }

    fun activeInjury(playerId: String):
        Injury? = injuries.values.firstOrNull {
            it.playerId == playerId &&
                it.status != InjuryStatus.CLEARED
        }

    fun activeInjuries(): List<Injury> =
        injuries.values.filter {
            it.status != InjuryStatus.CLEARED
        }

    fun injuryHistory(playerId: String):
        List<Injury> =
        injuries.values.filter {
            it.playerId == playerId
        }

    fun saveDiscipline(
        discipline: PlayerDiscipline
    ) {
        disciplines[
            disciplineKey(
                discipline.playerId,
                discipline.competitionId
            )
        ] = discipline
    }

    fun findDiscipline(
        playerId: String,
        competitionId: String
    ): PlayerDiscipline? =
        disciplines[
            disciplineKey(playerId, competitionId)
        ]

    fun disciplinesByCompetition(
        competitionId: String
    ): List<PlayerDiscipline> =
        disciplines.values.filter {
            it.competitionId == competitionId
        }

    fun saveSuspension(suspension: Suspension) {
        suspensions[suspension.id] = suspension
    }

    fun activeSuspensions(
        playerId: String
    ): List<Suspension> =
        suspensions.values.filter {
            it.playerId == playerId && it.isActive
        }

    private fun disciplineKey(
        playerId: String,
        competitionId: String
    ): String = "${playerId}_$competitionId"
}
