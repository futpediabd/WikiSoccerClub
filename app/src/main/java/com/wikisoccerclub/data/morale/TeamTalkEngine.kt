package com.wikisoccerclub.data.morale

enum class TeamTalkTone {
    CALM,
    MOTIVATIONAL,
    DEMANDING,
    PRAISE,
    CRITICAL
}

object TeamTalkEngine {

    fun beforeMatch(
        tone: TeamTalkTone,
        playerMorale: Int,
        opponentStrengthDifference: Int
    ): TeamTalkResult {
        require(playerMorale in 0..100)

        return when (tone) {
            TeamTalkTone.CALM ->
                TeamTalkResult(
                    moraleChange = if (
                        playerMorale < 40
                    ) 2 else 0,
                    managerRelationshipChange = 1,
                    message = "O treinador reduziu a pressão."
                )
            TeamTalkTone.MOTIVATIONAL ->
                TeamTalkResult(
                    moraleChange = 4,
                    managerRelationshipChange = 1,
                    message = "O elenco entrou mais motivado."
                )
            TeamTalkTone.DEMANDING ->
                TeamTalkResult(
                    moraleChange = if (
                        playerMorale >= 55
                    ) 3 else -2,
                    managerRelationshipChange = if (
                        playerMorale >= 55
                    ) 0 else -2,
                    message = "O treinador exigiu uma atuação forte."
                )
            TeamTalkTone.PRAISE ->
                TeamTalkResult(
                    moraleChange = 3,
                    managerRelationshipChange = 2,
                    message = "O elenco recebeu elogios."
                )
            TeamTalkTone.CRITICAL ->
                TeamTalkResult(
                    moraleChange = if (
                        opponentStrengthDifference <= -10
                    ) 2 else -4,
                    managerRelationshipChange = -2,
                    message = "O treinador criticou o desempenho."
                )
        }
    }
}
