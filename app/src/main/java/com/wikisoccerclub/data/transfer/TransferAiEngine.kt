package com.wikisoccerclub.data.transfer

import kotlin.math.roundToLong

object TransferAiEngine {

    fun evaluateTargets(
        club: AiClubProfile,
        players: List<MarketPlayer>,
        maxTargets: Int = 5
    ): List<AiTransferDecision> {
        if (maxTargets <= 0 || club.needs.isEmpty()) {
            return emptyList()
        }

        return players.asSequence()
            .filter { player ->
                player.playerId !in club.squadPlayerIds &&
                    player.clubId != club.clubId
            }
            .mapNotNull { player ->
                val need = bestNeedForPlayer(
                    club = club,
                    player = player
                ) ?: return@mapNotNull null

                val score = calculateScore(
                    club = club,
                    player = player,
                    need = need
                )

                if (score < minimumScore(need.priority)) {
                    return@mapNotNull null
                }

                val offerValue = calculateOfferValue(
                    player = player,
                    score = score
                )

                if (offerValue > club.balance) {
                    return@mapNotNull null
                }

                AiTransferDecision(
                    clubId = club.clubId,
                    playerId = player.playerId,
                    transferValue = offerValue,
                    proposedSalary = calculateSalary(player),
                    score = score,
                    reason = buildReason(need, player)
                )
            }
            .sortedByDescending { it.score }
            .take(maxTargets)
            .toList()
    }

    private fun bestNeedForPlayer(
        club: AiClubProfile,
        player: MarketPlayer
    ): SquadNeed? =
        club.needs
            .filter { need ->
                need.position.equals(
                    player.position,
                    ignoreCase = true
                ) &&
                    player.overall >= need.minimumOverall &&
                    (
                        need.maximumAge == null ||
                            player.age <= need.maximumAge
                        )
            }
            .maxByOrNull { priorityWeight(it.priority) }

    private fun calculateScore(
        club: AiClubProfile,
        player: MarketPlayer,
        need: SquadNeed
    ): Int {
        val priority = priorityWeight(need.priority)
        val overallScore = player.overall * 2
        val potentialBonus = (
            player.potential - player.overall
            ).coerceAtLeast(0)
        val ageBonus = when {
            player.age <= 21 -> 18
            player.age <= 25 -> 12
            player.age <= 29 -> 6
            player.age <= 32 -> 1
            else -> -8
        }
        val availabilityBonus = when {
            player.listedForSale -> 14
            player.clubId == null -> 20
            else -> 0
        }
        val reputationFit = (
            club.reputation - player.overall
            ).coerceIn(-15, 15)

        return priority +
            overallScore +
            potentialBonus +
            ageBonus +
            availabilityBonus +
            reputationFit
    }

    private fun calculateOfferValue(
        player: MarketPlayer,
        score: Int
    ): Long {
        if (player.clubId == null) return 0

        val multiplier = when {
            player.listedForSale -> 0.95
            score >= 220 -> 1.20
            score >= 190 -> 1.10
            else -> 1.00
        }

        return (player.marketValue * multiplier)
            .roundToLong()
            .coerceAtLeast(0)
    }

    private fun calculateSalary(
        player: MarketPlayer
    ): Long {
        val increase = when {
            player.clubId == null -> 1.15
            player.overall >= 85 -> 1.20
            else -> 1.10
        }

        return (player.monthlySalary * increase)
            .roundToLong()
            .coerceAtLeast(1)
    }

    private fun priorityWeight(
        priority: SquadNeedPriority
    ): Int = when (priority) {
        SquadNeedPriority.LOW -> 10
        SquadNeedPriority.MEDIUM -> 25
        SquadNeedPriority.HIGH -> 40
        SquadNeedPriority.URGENT -> 60
    }

    private fun minimumScore(
        priority: SquadNeedPriority
    ): Int = when (priority) {
        SquadNeedPriority.LOW -> 175
        SquadNeedPriority.MEDIUM -> 165
        SquadNeedPriority.HIGH -> 155
        SquadNeedPriority.URGENT -> 145
    }

    private fun buildReason(
        need: SquadNeed,
        player: MarketPlayer
    ): String {
        val availability = when {
            player.clubId == null -> "agente livre"
            player.listedForSale -> "listado para venda"
            player.availableForLoan -> "disponível para empréstimo"
            else -> "compatível com o elenco"
        }

        return "${need.priority}: necessidade em " +
            "${need.position}; jogador $availability."
    }
}
