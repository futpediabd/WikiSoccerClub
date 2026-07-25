package com.wikisoccerclub.data.competition.runtime

object RuntimeMatchResultValidator {

    fun validate(
        match: RuntimeMatch,
        result: RuntimeMatchResult
    ) {
        require(match.matchId == result.matchId) {
            "O resultado não pertence à partida informada."
        }

        require(match.status != RuntimeMatchStatus.FINISHED) {
            "A partida já foi encerrada."
        }

        require(result.homeGoals >= 0 && result.awayGoals >= 0) {
            "Os gols não podem ser negativos."
        }

        require(match.homeClubId != match.awayClubId) {
            "Um clube não pode enfrentar a si próprio."
        }
    }
}
