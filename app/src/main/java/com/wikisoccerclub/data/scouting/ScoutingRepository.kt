package com.wikisoccerclub.data.scouting

class ScoutingRepository {

    private val scouts =
        linkedMapOf<String, ScoutProfile>()

    private val assignments =
        linkedMapOf<String, ScoutingAssignment>()

    private val players =
        linkedMapOf<String, ScoutedPlayer>()

    private val reports =
        linkedMapOf<String, ScoutingReport>()

    fun saveScout(scout: ScoutProfile) {
        scouts[scout.id] = scout
    }

    fun removeScout(scoutId: String) {
        scouts.remove(scoutId)
    }

    fun allScouts(): List<ScoutProfile> =
        scouts.values.toList()

    fun findScout(scoutId: String): ScoutProfile? =
        scouts[scoutId]

    fun saveAssignment(
        assignment: ScoutingAssignment
    ) {
        assignments[assignment.id] = assignment
    }

    fun findAssignment(
        assignmentId: String
    ): ScoutingAssignment? =
        assignments[assignmentId]

    fun assignmentsByStatus(
        status: ScoutingStatus
    ): List<ScoutingAssignment> =
        assignments.values.filter {
            it.status == status
        }

    fun allAssignments():
        List<ScoutingAssignment> =
        assignments.values.toList()

    fun savePlayers(
        values: List<ScoutedPlayer>
    ) {
        values.forEach {
            players[it.playerId] = it
        }
    }

    fun allPlayers(): List<ScoutedPlayer> =
        players.values.toList()

    fun saveReports(
        values: List<ScoutingReport>
    ) {
        values.forEach {
            reports[it.id] = it
        }
    }

    fun reportByPlayer(
        playerId: String
    ): ScoutingReport? =
        reports.values
            .filter { it.playerId == playerId }
            .maxByOrNull { it.generatedDay }

    fun reportsByAssignment(
        assignmentId: String
    ): List<ScoutingReport> =
        reports.values
            .filter {
                it.assignmentId == assignmentId
            }
            .sortedByDescending {
                it.recommendationScore
            }

    fun allReports(): List<ScoutingReport> =
        reports.values
            .sortedByDescending {
                it.recommendationScore
            }
}
