package com.wikisoccerclub.data.board

class BoardRepository {

    private val objectives =
        linkedMapOf<String, BoardObjective>()

    private val clubReputations =
        linkedMapOf<String, ClubReputation>()

    private val managerReputations =
        linkedMapOf<String, ManagerReputation>()

    private val evaluations =
        mutableListOf<BoardEvaluation>()

    fun saveObjective(objective: BoardObjective) {
        objectives[objective.id] = objective
    }

    fun objectivesByClub(
        clubId: String,
        seasonYear: Int
    ): List<BoardObjective> =
        objectives.values.filter {
            it.clubId == clubId &&
                it.seasonYear == seasonYear
        }

    fun findObjective(id: String):
        BoardObjective? = objectives[id]

    fun saveClubReputation(
        reputation: ClubReputation
    ) {
        clubReputations[reputation.clubId] =
            reputation
    }

    fun findClubReputation(
        clubId: String
    ): ClubReputation? =
        clubReputations[clubId]

    fun saveManagerReputation(
        reputation: ManagerReputation
    ) {
        managerReputations[reputation.managerId] =
            reputation
    }

    fun findManagerReputation(
        managerId: String
    ): ManagerReputation? =
        managerReputations[managerId]

    fun saveEvaluation(
        evaluation: BoardEvaluation
    ) {
        evaluations.removeAll {
            it.clubId == evaluation.clubId &&
                it.seasonYear == evaluation.seasonYear
        }
        evaluations += evaluation
    }

    fun latestEvaluation(
        clubId: String
    ): BoardEvaluation? =
        evaluations
            .filter { it.clubId == clubId }
            .maxByOrNull { it.seasonYear }
}
