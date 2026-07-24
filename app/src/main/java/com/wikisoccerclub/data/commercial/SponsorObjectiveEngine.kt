package com.wikisoccerclub.data.commercial

object SponsorObjectiveEngine {

    fun updateProgress(
        contract: SponsorContract,
        type: SponsorObjectiveType,
        value: Int,
        absoluteValue: Boolean = false
    ): SponsorContract {
        require(value >= 0)

        val updated = contract.objectives.map {
            if (
                it.type != type ||
                it.completed ||
                it.failed
            ) {
                it
            } else {
                val progress =
                    if (absoluteValue) {
                        value
                    } else {
                        it.currentValue + value
                    }

                val complete =
                    objectiveReached(
                        type = it.type,
                        target = it.targetValue,
                        progress = progress
                    )

                it.copy(
                    currentValue = progress,
                    completed = complete
                )
            }
        }

        return contract.copy(objectives = updated)
    }

    fun settleSeason(
        contract: SponsorContract,
        seasonYear: Int
    ): SponsorContract {
        if (
            contract.status !=
                ContractStatus.ACTIVE
        ) return contract

        val settledObjectives =
            contract.objectives.map {
                if (it.completed) it
                else it.copy(failed = true)
            }

        val bonus =
            settledObjectives
                .filter { it.completed }
                .sumOf { it.bonusValue }

        val penalty =
            settledObjectives
                .filter { it.failed }
                .sumOf { it.penaltyValue }

        val annualPayment =
            contract.fixedAnnualValue

        val newStatus =
            if (seasonYear >= contract.endSeason) {
                ContractStatus.COMPLETED
            } else {
                ContractStatus.ACTIVE
            }

        return contract.copy(
            objectives = settledObjectives,
            status = newStatus,
            totalReceived =
                contract.totalReceived +
                    annualPayment +
                    bonus -
                    penalty
        )
    }

    fun financialSettlement(
        before: SponsorContract,
        after: SponsorContract
    ): Long =
        after.totalReceived -
            before.totalReceived

    private fun objectiveReached(
        type: SponsorObjectiveType,
        target: Int,
        progress: Int
    ): Boolean =
        when (type) {
            SponsorObjectiveType.LEAGUE_POSITION ->
                progress in 1..target
            else ->
                progress >= target
        }
}
