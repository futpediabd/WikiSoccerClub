package com.wikisoccerclub.data.competition.runtime

object CompetitionRuntimeEngine {

    fun startMatch(match: RuntimeMatch): RuntimeMatch {
        require(match.status == RuntimeMatchStatus.SCHEDULED) {
            "Somente partidas programadas podem ser iniciadas."
        }

        return match.copy(status = RuntimeMatchStatus.IN_PROGRESS)
    }

    fun finishMatch(
        match: RuntimeMatch,
        result: RuntimeMatchResult
    ): RuntimeMatch {
        RuntimeMatchResultValidator.validate(match, result)

        return match.copy(
            status = RuntimeMatchStatus.FINISHED,
            homeGoals = result.homeGoals,
            awayGoals = result.awayGoals
        )
    }

    fun postponeMatch(match: RuntimeMatch): RuntimeMatch {
        require(match.status != RuntimeMatchStatus.FINISHED) {
            "Uma partida encerrada não pode ser adiada."
        }

        return match.copy(status = RuntimeMatchStatus.POSTPONED)
    }

    fun rescheduleMatch(
        match: RuntimeMatch,
        newDay: Int
    ): RuntimeMatch {
        require(newDay > match.day) {
            "A nova data deve ser posterior à data original."
        }

        return match.copy(
            day = newDay,
            status = RuntimeMatchStatus.SCHEDULED
        )
    }
}
