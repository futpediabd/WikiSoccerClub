package com.wikisoccerclub.data.youth

class YouthAcademyRepository {

    private val academies =
        linkedMapOf<String, YouthAcademy>()

    private val prospects =
        linkedMapOf<String, YouthProspect>()

    private val intakes =
        mutableListOf<YouthIntakeResult>()

    fun saveAcademy(academy: YouthAcademy) {
        academies[academy.clubId] = academy
    }

    fun findAcademy(clubId: String):
        YouthAcademy? = academies[clubId]

    fun saveProspect(prospect: YouthProspect) {
        prospects[prospect.id] = prospect
    }

    fun saveProspects(
        values: List<YouthProspect>
    ) {
        values.forEach(::saveProspect)
    }

    fun findProspect(id: String):
        YouthProspect? = prospects[id]

    fun prospects(): List<YouthProspect> =
        prospects.values.toList()

    fun academyProspects(
        clubId: String
    ): List<YouthProspect> {
        val ids =
            academies[clubId]?.prospectIds.orEmpty()

        return ids.mapNotNull(prospects::get)
    }

    fun saveIntake(result: YouthIntakeResult) {
        intakes += result
        saveProspects(result.prospects)
    }

    fun intakeHistory(
        clubId: String
    ): List<YouthIntakeResult> =
        intakes.filter {
            it.clubId == clubId
        }.sortedByDescending {
            it.seasonYear
        }
}
