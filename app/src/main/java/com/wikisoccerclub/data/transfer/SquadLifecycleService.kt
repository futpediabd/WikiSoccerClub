package com.wikisoccerclub.data.transfer

import com.wikisoccerclub.data.youth.YouthAcademyRepository
import com.wikisoccerclub.data.youth.YouthProspectStatus

/**
 * Orquestra a gestão automática do elenco ao final/início de cada temporada.
 * As decisões são persistidas nos repositórios compartilhados do módulo.
 */
class SquadLifecycleService(
    private val contracts: ContractRepository,
    private val youth: YouthAcademyRepository
) {
    fun processSeason(
        clubId: String,
        seasonYear: Int,
        squad: List<LifecycleSquadPlayer>,
        needs: List<SquadNeed>,
        maximumSquadSize: Int = 35
    ): SquadLifecycleResult {
        val renewalDecisions = SquadLifecycleEngine.analyzeRenewals(squad, seasonYear)
        val renewed = renewalDecisions.mapNotNull { decision ->
            if (!decision.renew) return@mapNotNull null
            val current = contracts.findContract(decision.playerId) ?: return@mapNotNull null
            current.copy(
                endYear = decision.proposedEndYear ?: current.endYear,
                monthlySalary = decision.proposedMonthlySalary ?: current.monthlySalary
            ).also(contracts::saveContract)
        }

        val promotionDecisions = SquadLifecycleEngine.analyzeYouthPromotions(
            prospects = youth.academyProspects(clubId),
            squad = squad,
            maximumSquadSize = maximumSquadSize
        )
        val promoted = promotionDecisions.filter { it.promote }.map { decision ->
            decision.prospect.copy(status = YouthProspectStatus.PROMOTED)
                .also(youth::saveProspect)
        }

        val updatedSize = squad.size + promoted.size
        val saleDecisions = SquadLifecycleEngine.analyzeSurplusPlayers(
            squad = squad,
            needs = needs,
            maximumSquadSize = (maximumSquadSize - promoted.size).coerceAtLeast(0)
        ).filter { it.listForSale }

        val actions = buildList {
            renewed.forEach {
                add(LifecycleAction(
                    LifecycleActionType.CONTRACT_RENEWAL,
                    it.playerId,
                    "Contrato renovado até ${it.endYear}."
                ))
            }
            promoted.forEach {
                add(LifecycleAction(
                    LifecycleActionType.YOUTH_PROMOTION,
                    it.id,
                    "${it.name} promovido ao elenco principal."
                ))
            }
            saleDecisions.forEach {
                add(LifecycleAction(
                    LifecycleActionType.PLAYER_LISTED_FOR_SALE,
                    it.playerId,
                    "Atleta colocado à venda por ${it.askingPrice}."
                ))
            }
        }

        check(updatedSize <= maximumSquadSize || saleDecisions.isNotEmpty()) {
            "O elenco ultrapassou o limite e nenhum excedente foi identificado."
        }

        return SquadLifecycleResult(
            clubId = clubId,
            seasonYear = seasonYear,
            renewedContracts = renewed,
            promotedProspects = promoted,
            listedPlayers = saleDecisions,
            actions = actions
        )
    }
}
