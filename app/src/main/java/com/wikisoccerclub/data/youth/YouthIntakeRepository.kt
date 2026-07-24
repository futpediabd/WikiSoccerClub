package com.wikisoccerclub.data.youth

class YouthIntakeRepository {

    private val intakes =
        linkedMapOf<String, YouthIntakeResult>()

    private val projections =
        linkedMapOf<String, YouthDevelopmentProjection>()

    fun saveIntake(
        result: YouthIntakeResult
    ) {
        intakes[key(
            result.clubId,
            result.seasonYear
        )] = result
    }

    fun findIntake(
        clubId: String,
        seasonYear: Int
    ): YouthIntakeResult? =
        intakes[key(clubId, seasonYear)]

    fun allIntakes(
        clubId: String
    ): List<YouthIntakeResult> =
        intakes.values
            .filter {
                it.clubId == clubId
            }
            .sortedByDescending {
                it.seasonYear
            }

    fun saveProjection(
        projection: YouthDevelopmentProjection
    ) {
        projections[
            projection.playerId
        ] = projection
    }

    fun saveProjections(
        values: List<YouthDevelopmentProjection>
    ) {
        values.forEach(::saveProjection)
    }

    fun projectionByPlayer(
        playerId: String
    ): YouthDevelopmentProjection? =
        projections[playerId]

    fun allProjections():
        List<YouthDevelopmentProjection> =
        projections.values.toList()

    private fun key(
        clubId: String,
        seasonYear: Int
    ): String =
        "${clubId}_$seasonYear"
}
