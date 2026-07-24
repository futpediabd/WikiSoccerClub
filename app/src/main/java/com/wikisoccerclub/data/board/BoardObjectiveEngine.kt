package com.wikisoccerclub.data.board

object BoardObjectiveEngine {

    fun validate(
        objective: BoardObjective
    ): Result<Unit> = runCatching {
        require(objective.clubId.isNotBlank()) {
            "O clube precisa ser informado."
        }
        require(objective.title.isNotBlank()) {
            "O objetivo precisa ter um título."
        }
        require(objective.targetValue >= 0) {
            "A meta não pode ser negativa."
        }
        require(objective.currentValue >= 0) {
            "O progresso não pode ser negativo."
        }
        require(objective.weight in 1..100) {
            "O peso deve ficar entre 1 e 100."
        }
    }

    fun updateProgress(
        objective: BoardObjective,
        value: Int,
        currentDay: Int? = null
    ): BoardObjective {
        validate(objective).getOrThrow()
        require(value >= 0) {
            "O progresso não pode ser negativo."
        }

        if (
            objective.status !=
                BoardObjectiveStatus.ACTIVE
        ) {
            return objective
        }

        if (
            currentDay != null &&
            objective.deadlineDay != null &&
            currentDay > objective.deadlineDay
        ) {
            return finishAtDeadline(objective, value)
        }

        val completed = isCompleted(
            type = objective.type,
            currentValue = value,
            targetValue = objective.targetValue
        )

        return objective.copy(
            currentValue = value,
            status = if (completed) {
                BoardObjectiveStatus.COMPLETED
            } else {
                BoardObjectiveStatus.ACTIVE
            }
        )
    }

    fun finishAtDeadline(
        objective: BoardObjective,
        finalValue: Int = objective.currentValue
    ): BoardObjective {
        if (
            objective.status !=
                BoardObjectiveStatus.ACTIVE
        ) {
            return objective
        }

        val completed = isCompleted(
            type = objective.type,
            currentValue = finalValue,
            targetValue = objective.targetValue
        )

        return objective.copy(
            currentValue = finalValue,
            status = if (completed) {
                BoardObjectiveStatus.COMPLETED
            } else {
                BoardObjectiveStatus.FAILED
            }
        )
    }

    private fun isCompleted(
        type: BoardObjectiveType,
        currentValue: Int,
        targetValue: Int
    ): Boolean = when (type) {
        BoardObjectiveType.LEAGUE_POSITION ->
            currentValue in 1..targetValue
        BoardObjectiveType.REDUCE_WAGE_BILL ->
            currentValue <= targetValue
        BoardObjectiveType.AVOID_RELEGATION ->
            currentValue == 1
        BoardObjectiveType.POSITIVE_BALANCE ->
            currentValue >= targetValue
        else -> currentValue >= targetValue
    }
}
