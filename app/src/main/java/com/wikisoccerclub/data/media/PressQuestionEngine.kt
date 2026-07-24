package com.wikisoccerclub.data.media

object PressQuestionEngine {

    fun generate(
        managerId: String,
        day: Int,
        hasUpcomingRivalry: Boolean,
        boardConfidence: Int,
        transferWindowOpen: Boolean,
        recentFormScore: Int
    ): List<PressQuestion> {
        val questions = mutableListOf<PressQuestion>()

        questions += PressQuestion(
            id = "press_${managerId}_${day}_forma",
            type =
                PressQuestionType.TEAM_PERFORMANCE,
            question =
                if (recentFormScore >= 65) {
                    "A equipe vive boa fase. Qual é o segredo?"
                } else {
                    "Como pretende reagir aos resultados recentes?"
                }
        )

        if (hasUpcomingRivalry) {
            questions += PressQuestion(
                id =
                    "press_${managerId}_${day}_classico",
                type = PressQuestionType.RIVALRY,
                question =
                    "O que espera do próximo clássico?"
            )
        }

        if (boardConfidence < 40) {
            questions += PressQuestion(
                id =
                    "press_${managerId}_${day}_diretoria",
                type =
                    PressQuestionType.BOARD_PRESSURE,
                question =
                    "Você se sente pressionado pela diretoria?"
            )
        }

        if (transferWindowOpen) {
            questions += PressQuestion(
                id =
                    "press_${managerId}_${day}_mercado",
                type = PressQuestionType.TRANSFER,
                question =
                    "O clube pretende contratar novos jogadores?"
            )
        }

        return questions.take(4)
    }
}
