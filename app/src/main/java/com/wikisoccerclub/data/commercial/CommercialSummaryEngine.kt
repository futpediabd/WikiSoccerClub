package com.wikisoccerclub.data.commercial

object CommercialSummaryEngine {

    fun build(
        contracts: List<SponsorContract>,
        merchandiseResults: List<MerchandiseResult>
    ): CommercialSummary {
        val active = contracts.filter {
            it.status == ContractStatus.ACTIVE
        }

        val annual =
            active.sumOf {
                it.fixedAnnualValue
            }

        val bonuses =
            contracts.sumOf { contract ->
                contract.objectives
                    .filter { it.completed }
                    .sumOf { it.bonusValue }
            }

        val merchandise =
            merchandiseResults.sumOf {
                it.netRevenue
            }

        return CommercialSummary(
            activeContracts = active,
            annualSponsorIncome = annual,
            objectiveBonuses = bonuses,
            merchandiseRevenue = merchandise,
            totalCommercialRevenue =
                annual + bonuses + merchandise
        )
    }
}
