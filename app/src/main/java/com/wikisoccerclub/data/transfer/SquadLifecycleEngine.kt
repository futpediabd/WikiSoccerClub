package com.wikisoccerclub.data.transfer

import com.wikisoccerclub.data.youth.YouthProspect
import com.wikisoccerclub.data.youth.YouthProspectStatus

/** Regras puras da Etapa 4.3 para renovação, base e jogadores excedentes. */
object SquadLifecycleEngine {

    fun analyzeRenewals(
        squad: List<LifecycleSquadPlayer>,
        currentYear: Int,
        maxContractYears: Int = 4
    ): List<ContractRenewalDecision> = squad
        .filter { it.contractEndYear != null && it.contractEndYear <= currentYear + 1 }
        .map { player ->
            val important = player.starter || player.appearances >= 15
            val promising = player.age <= 23 && player.potential >= player.overall + 3
            val declineRisk = player.age >= 34 && !important
            val renew = !declineRisk && (important || promising || player.overall >= 65)
            val years = when {
                !renew -> null
                player.age <= 24 -> maxContractYears
                player.age <= 29 -> 3
                player.age <= 32 -> 2
                else -> 1
            }
            val raisePercent = when {
                player.starter && player.appearances >= 25 -> 20
                important -> 12
                promising -> 8
                else -> 5
            }
            ContractRenewalDecision(
                playerId = player.playerId,
                renew = renew,
                proposedEndYear = years?.let { currentYear + it },
                proposedMonthlySalary = if (renew) {
                    (player.monthlySalary.coerceAtLeast(1L) * (100 + raisePercent)) / 100
                } else null,
                reason = when {
                    declineRisk -> "Atleta veterano sem papel relevante no elenco."
                    player.starter -> "Titular importante para a próxima temporada."
                    promising -> "Jovem com potencial de evolução."
                    renew -> "Peça útil para a composição do elenco."
                    else -> "Renovação não recomendada pela IA."
                }
            )
        }

    fun analyzeYouthPromotions(
        prospects: List<YouthProspect>,
        squad: List<LifecycleSquadPlayer>,
        maximumSquadSize: Int = 35,
        minimumOverall: Int = 55
    ): List<YouthPromotionDecision> {
        var availableSlots = (maximumSquadSize - squad.size).coerceAtLeast(0)
        return prospects
            .filter { it.status == YouthProspectStatus.ACADEMY }
            .sortedWith(compareByDescending<YouthProspect> { it.potential }.thenByDescending { it.overall })
            .map { prospect ->
                val ready = prospect.age >= 17 &&
                    prospect.overall >= minimumOverall &&
                    prospect.potential >= prospect.overall + 2
                val promote = ready && availableSlots > 0
                if (promote) availableSlots--
                YouthPromotionDecision(
                    prospect = prospect,
                    promote = promote,
                    reason = when {
                        availableSlots < 0 -> "Elenco principal sem vaga disponível."
                        prospect.age < 17 -> "Atleta ainda muito jovem para o elenco principal."
                        prospect.overall < minimumOverall -> "Overall abaixo do mínimo para promoção."
                        prospect.potential < prospect.overall + 2 -> "Potencial de evolução insuficiente."
                        promote -> "Atleta pronto para integrar o elenco principal."
                        else -> "Elenco principal sem vaga disponível."
                    }
                )
            }
    }

    fun analyzeSurplusPlayers(
        squad: List<LifecycleSquadPlayer>,
        needs: List<SquadNeed>,
        maximumSquadSize: Int = 35
    ): List<SurplusPlayerDecision> {
        val protectedPositions = needs
            .filter { it.priority == SquadNeedPriority.HIGH || it.priority == SquadNeedPriority.URGENT }
            .map { normalize(it.position) }
            .toSet()
        val averageOverall = if (squad.isEmpty()) 0 else squad.map { it.overall }.average().toInt()
        val excess = (squad.size - maximumSquadSize).coerceAtLeast(0)

        return squad
            .filterNot { it.transferListed }
            .filterNot { normalize(it.position) in protectedPositions }
            .sortedWith(
                compareBy<LifecycleSquadPlayer> { it.starter }
                    .thenBy { it.appearances }
                    .thenBy { it.overall }
                    .thenByDescending { it.age }
            )
            .mapIndexed { index, player ->
                val clearlySurplus = !player.starter && player.appearances < 8 &&
                    (player.overall <= averageOverall - 5 || player.age >= 31)
                val list = index < excess || clearlySurplus
                val multiplier = when {
                    player.age <= 23 && player.potential > player.overall -> 120
                    player.age >= 32 -> 80
                    else -> 100
                }
                SurplusPlayerDecision(
                    playerId = player.playerId,
                    listForSale = list,
                    askingPrice = (player.marketValue.coerceAtLeast(0L) * multiplier) / 100,
                    reason = when {
                        index < excess -> "Elenco acima do limite de $maximumSquadSize atletas."
                        clearlySurplus -> "Pouca utilização e baixo encaixe no planejamento."
                        else -> "Atleta mantido no planejamento do clube."
                    }
                )
            }
    }

    private fun normalize(value: String): String = value.trim().uppercase()
}
