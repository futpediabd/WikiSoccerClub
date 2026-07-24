package com.wikisoccerclub.data.transfer

import com.wikisoccerclub.data.youth.YouthProspect

enum class LifecycleActionType {
    CONTRACT_RENEWAL,
    YOUTH_PROMOTION,
    PLAYER_LISTED_FOR_SALE
}

data class LifecycleSquadPlayer(
    val playerId: String,
    val clubId: String,
    val position: String,
    val age: Int,
    val overall: Int,
    val potential: Int = overall,
    val marketValue: Long = 0,
    val monthlySalary: Long = 0,
    val appearances: Int = 0,
    val starter: Boolean = false,
    val contractEndYear: Int? = null,
    val transferListed: Boolean = false
)

data class ContractRenewalDecision(
    val playerId: String,
    val renew: Boolean,
    val proposedEndYear: Int?,
    val proposedMonthlySalary: Long?,
    val reason: String
)

data class YouthPromotionDecision(
    val prospect: YouthProspect,
    val promote: Boolean,
    val reason: String
)

data class SurplusPlayerDecision(
    val playerId: String,
    val listForSale: Boolean,
    val askingPrice: Long,
    val reason: String
)

data class SquadLifecycleResult(
    val clubId: String,
    val seasonYear: Int,
    val renewedContracts: List<PlayerContract>,
    val promotedProspects: List<YouthProspect>,
    val listedPlayers: List<SurplusPlayerDecision>,
    val actions: List<LifecycleAction>
)

data class LifecycleAction(
    val type: LifecycleActionType,
    val entityId: String,
    val description: String
)
