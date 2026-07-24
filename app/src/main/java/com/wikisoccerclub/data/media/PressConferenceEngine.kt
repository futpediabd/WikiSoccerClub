package com.wikisoccerclub.data.media

object PressConferenceEngine {

    fun answer(
        question: PressQuestion,
        tone: PressAnswerTone,
        recentFormScore: Int,
        squadMorale: Int,
        boardConfidence: Int
    ): PressAnswerResult {
        require(recentFormScore in 0..100)
        require(squadMorale in 0..100)
        require(boardConfidence in 0..100)

        val base = effectsForTone(tone)
        val contextAdjustment =
            contextAdjustment(
                question = question,
                tone = tone,
                recentFormScore = recentFormScore,
                squadMorale = squadMorale,
                boardConfidence = boardConfidence
            )

        return PressAnswerResult(
            questionId = question.id,
            tone = tone,
            boardConfidenceChange =
                (base.board + contextAdjustment.board)
                    .coerceIn(-10, 10),
            squadMoraleChange =
                (base.morale + contextAdjustment.morale)
                    .coerceIn(-10, 10),
            managerReputationChange =
                (base.reputation +
                    contextAdjustment.reputation)
                    .coerceIn(-10, 10),
            mediaRelationshipChange =
                (base.media + contextAdjustment.media)
                    .coerceIn(-10, 10),
            responseText =
                responseText(question.type, tone)
        )
    }

    fun updateMediaProfile(
        profile: MediaProfile,
        result: PressAnswerResult
    ): MediaProfile =
        profile.copy(
            mediaRelationship = (
                profile.mediaRelationship +
                    result.mediaRelationshipChange
                ).coerceIn(0, 100),
            pressReputation = (
                profile.pressReputation +
                    result.managerReputationChange
                ).coerceIn(0, 100),
            interviewsGiven =
                profile.interviewsGiven + 1,
            controversialAnswers =
                profile.controversialAnswers +
                    if (
                        result.tone ==
                            PressAnswerTone.AGGRESSIVE ||
                        result.tone ==
                            PressAnswerTone.CRITICAL
                    ) 1 else 0
        )

    private data class Effects(
        val board: Int,
        val morale: Int,
        val reputation: Int,
        val media: Int
    )

    private fun effectsForTone(
        tone: PressAnswerTone
    ): Effects = when (tone) {
        PressAnswerTone.CALM ->
            Effects(1, 1, 0, 1)
        PressAnswerTone.CONFIDENT ->
            Effects(1, 2, 2, 1)
        PressAnswerTone.RESPECTFUL ->
            Effects(1, 1, 1, 3)
        PressAnswerTone.MOTIVATIONAL ->
            Effects(0, 4, 1, 1)
        PressAnswerTone.CRITICAL ->
            Effects(0, -3, 1, -1)
        PressAnswerTone.AGGRESSIVE ->
            Effects(-2, -2, 2, -4)
    }

    private fun contextAdjustment(
        question: PressQuestion,
        tone: PressAnswerTone,
        recentFormScore: Int,
        squadMorale: Int,
        boardConfidence: Int
    ): Effects {
        var board = 0
        var morale = 0
        var reputation = 0
        var media = 0

        if (
            tone == PressAnswerTone.CONFIDENT &&
            recentFormScore < 35
        ) {
            reputation -= 2
            media -= 1
        }

        if (
            tone == PressAnswerTone.CRITICAL &&
            squadMorale < 40
        ) {
            morale -= 3
        }

        if (
            question.type ==
                PressQuestionType.BOARD_PRESSURE &&
            tone == PressAnswerTone.AGGRESSIVE
        ) {
            board -= 4
        }

        if (
            question.type ==
                PressQuestionType.RIVALRY &&
            tone == PressAnswerTone.MOTIVATIONAL
        ) {
            morale += 2
        }

        if (
            boardConfidence < 30 &&
            tone == PressAnswerTone.RESPECTFUL
        ) {
            board += 2
        }

        return Effects(
            board,
            morale,
            reputation,
            media
        )
    }

    private fun responseText(
        type: PressQuestionType,
        tone: PressAnswerTone
    ): String =
        "Resposta ${tone.name.lowercase()} sobre " +
            type.name.lowercase()
}
