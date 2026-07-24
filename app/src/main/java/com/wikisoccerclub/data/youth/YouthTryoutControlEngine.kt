package com.wikisoccerclub.data.youth

object YouthTryoutControlEngine {

    fun availability(
        position: YouthPositionFilter,
        currentDay: Int,
        control: YouthTryoutControl,
        settings: YouthTryoutSettings
    ): YouthTryoutAvailability {
        require(currentDay >= 0)
        require(settings.cooldownDays >= 0)

        val lastDay = control.lastTryoutDay
        val elapsed =
            if (lastDay == null) {
                settings.cooldownDays
            } else {
                currentDay - lastDay
            }

        val remaining =
            (settings.cooldownDays - elapsed)
                .coerceAtLeast(0)

        val available = remaining == 0

        return YouthTryoutAvailability(
            available = available,
            remainingDays = remaining,
            cost = settings.costFor(position),
            message =
                if (available) {
                    "Peneira disponível."
                } else {
                    "Aguarde $remaining dia(s) para realizar outra peneira."
                }
        )
    }

    fun canPay(
        clubBalance: Long,
        cost: Long
    ): Boolean =
        clubBalance >= cost

    fun updatedBalanceAfterTryout(
        clubBalance: Long,
        cost: Long
    ): Long {
        require(cost >= 0L)
        require(canPay(clubBalance, cost)) {
            "Saldo insuficiente para realizar a peneira."
        }

        return clubBalance - cost
    }

    fun isResultExpired(
        result: YouthTryoutResult,
        currentDay: Int,
        settings: YouthTryoutSettings
    ): Boolean =
        currentDay - result.generatedDay >
            settings.resultValidityDays

    fun remainingValidityDays(
        result: YouthTryoutResult,
        currentDay: Int,
        settings: YouthTryoutSettings
    ): Int =
        (
            settings.resultValidityDays -
                (currentDay - result.generatedDay)
            ).coerceAtLeast(0)

    fun toSignedPlayer(
        candidate: YouthCandidate,
        contractYears: Int = 3
    ): YouthSignedPlayer {
        require(
            candidate.status ==
                YouthPlayerStatus.SIGNED
        ) {
            "O candidato precisa estar contratado."
        }

        require(contractYears in 1..5)

        return YouthSignedPlayer(
            id = candidate.id,
            name = candidate.name,
            nationality = candidate.nationality,
            age = candidate.age,
            positionGroup = candidate.position,
            positionName =
                candidate.specificPosition,
            overall = candidate.overall,
            potential = candidate.potential,
            salary = candidate.salaryRequest,
            contractYears = contractYears
        )
    }
}
