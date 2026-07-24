package com.wikisoccerclub.data.board

object BoardEvaluationEngine {

    fun evaluate(
        clubId: String,
        seasonYear: Int,
        objectives: List<BoardObjective>,
        leagueFormScore: Int,
        financialScore: Int,
        dressingRoomScore: Int
    ): BoardEvaluation {
        require(leagueFormScore in 0..100)
        require(financialScore in 0..100)
        require(dressingRoomScore in 0..100)

        val relevant = objectives.filter {
            it.clubId == clubId &&
                it.seasonYear == seasonYear
        }

        val objectiveScore =
            calculateObjectiveScore(relevant)

        val confidence = (
            objectiveScore * 0.45 +
                leagueFormScore * 0.30 +
                financialScore * 0.15 +
                dressingRoomScore * 0.10
            ).toInt().coerceIn(0, 100)

        val level = when {
            confidence >= 85 ->
                BoardConfidenceLevel.VERY_HIGH
            confidence >= 70 ->
                BoardConfidenceLevel.HIGH
            confidence >= 50 ->
                BoardConfidenceLevel.STABLE
            confidence >= 30 ->
                BoardConfidenceLevel.LOW
            else ->
                BoardConfidenceLevel.VERY_LOW
        }

        return BoardEvaluation(
            clubId = clubId,
            seasonYear = seasonYear,
            confidence = confidence,
            confidenceLevel = level,
            completedObjectives = relevant.count {
                it.status ==
                    BoardObjectiveStatus.COMPLETED
            },
            failedObjectives = relevant.count {
                it.status ==
                    BoardObjectiveStatus.FAILED
            },
            message = messageFor(level),
            dismissalRisk = confidence < 25
        )
    }

    private fun calculateObjectiveScore(
        objectives: List<BoardObjective>
    ): Int {
        if (objectives.isEmpty()) return 50

        val totalWeight =
            objectives.sumOf { it.weight }
                .coerceAtLeast(1)

        val weighted = objectives.sumOf {
            val score = when (it.status) {
                BoardObjectiveStatus.COMPLETED -> 100
                BoardObjectiveStatus.ACTIVE -> {
                    if (it.targetValue == 0) {
                        50
                    } else {
                        (
                            it.currentValue.toDouble() /
                                it.targetValue * 100
                            ).toInt().coerceIn(0, 100)
                    }
                }
                BoardObjectiveStatus.FAILED -> 0
                BoardObjectiveStatus.CANCELLED -> 50
            }
            score * it.weight
        }

        return weighted / totalWeight
    }

    private fun messageFor(
        level: BoardConfidenceLevel
    ): String = when (level) {
        BoardConfidenceLevel.VERY_HIGH ->
            "A diretoria está extremamente satisfeita."
        BoardConfidenceLevel.HIGH ->
            "A diretoria aprova o trabalho realizado."
        BoardConfidenceLevel.STABLE ->
            "A diretoria considera o trabalho estável."
        BoardConfidenceLevel.LOW ->
            "A diretoria está preocupada com os resultados."
        BoardConfidenceLevel.VERY_LOW ->
            "O cargo do treinador está seriamente ameaçado."
    }
}
