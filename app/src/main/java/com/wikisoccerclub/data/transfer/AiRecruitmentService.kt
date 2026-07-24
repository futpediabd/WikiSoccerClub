package com.wikisoccerclub.data.transfer

/** Resultado de uma rodada de planejamento da IA. */
data class AiRecruitmentPlan(
    val clubId: String,
    val date: CareerDate,
    val needs: List<SquadNeed>,
    val targets: List<AiTransferDecision>,
    val blockedReason: String? = null
) {
    val canNegotiate: Boolean get() = blockedReason == null
}

/**
 * Orquestra análise do elenco, busca de reforços e definição de prioridades.
 * Nenhuma proposta é criada fora da janela de transferências.
 */
class AiRecruitmentService(
    private val aiRepository: TransferAiRepository,
    private val windowRepository: TransferWindowRepository
) {
    fun plan(
        clubId: String,
        balance: Long,
        reputation: Int,
        squad: List<AiSquadPlayer>,
        market: List<MarketPlayer>,
        currentDate: CareerDate,
        requirements: List<PositionRequirement> = SquadNeedAnalyzer.defaultRequirements,
        maxTargets: Int = 5
    ): AiRecruitmentPlan {
        val needs = SquadNeedAnalyzer.analyze(
            clubId = clubId,
            squad = squad,
            requirements = requirements
        )

        val profile = AiClubProfile(
            clubId = clubId,
            balance = balance,
            reputation = reputation,
            squadPlayerIds = squad.map { it.playerId },
            needs = needs
        )
        aiRepository.saveClub(profile)
        aiRepository.savePlayers(market)

        if (!windowRepository.isOpen()) {
            return AiRecruitmentPlan(
                clubId = clubId,
                date = currentDate,
                needs = needs,
                targets = emptyList(),
                blockedReason = "A janela de transferências está fechada. A IA apenas atualizou o planejamento."
            )
        }

        val targets = TransferAiEngine.evaluateTargets(
            club = profile,
            players = market,
            maxTargets = maxTargets
        )
        targets.forEach(aiRepository::saveDecision)

        return AiRecruitmentPlan(
            clubId = clubId,
            date = currentDate,
            needs = needs,
            targets = targets
        )
    }

    fun createOffers(
        plan: AiRecruitmentPlan,
        seasonYear: Int,
        workflow: com.wikisoccerclub.core.transfer.TransferWorkflowService
    ): List<Result<TransferOffer>> {
        if (!plan.canNegotiate) return emptyList()

        return plan.targets.map { target ->
            workflow.createOffer(
                TransferOffer(
                    id = "AI-${plan.clubId}-${target.playerId}-$seasonYear",
                    playerId = target.playerId,
                    buyingClubId = plan.clubId,
                    sellingClubId = aiRepository.players()
                        .firstOrNull { it.playerId == target.playerId }
                        ?.clubId.orEmpty(),
                    value = target.transferValue,
                    status = OfferStatus.PENDING
                )
            )
        }
    }
}
