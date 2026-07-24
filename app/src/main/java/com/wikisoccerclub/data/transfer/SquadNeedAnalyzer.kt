package com.wikisoccerclub.data.transfer

/** Dados mínimos usados pela IA para avaliar a composição do elenco. */
data class AiSquadPlayer(
    val playerId: String,
    val position: String,
    val age: Int,
    val overall: Int,
    val contractMonthsRemaining: Int = 24,
    val injuredDays: Int = 0,
    val suspendedMatches: Int = 0
)

data class PositionRequirement(
    val position: String,
    val idealCount: Int,
    val starterCount: Int = 1
)

/**
 * Identifica posições carentes sem depender da interface.
 * A prioridade considera quantidade, qualidade, disponibilidade, idade e contrato.
 */
object SquadNeedAnalyzer {

    val defaultRequirements = listOf(
        PositionRequirement("GOL", idealCount = 3, starterCount = 1),
        PositionRequirement("LD", idealCount = 2, starterCount = 1),
        PositionRequirement("ZAG", idealCount = 4, starterCount = 2),
        PositionRequirement("LE", idealCount = 2, starterCount = 1),
        PositionRequirement("VOL", idealCount = 3, starterCount = 1),
        PositionRequirement("MC", idealCount = 4, starterCount = 2),
        PositionRequirement("MEI", idealCount = 3, starterCount = 1),
        PositionRequirement("PD", idealCount = 2, starterCount = 1),
        PositionRequirement("PE", idealCount = 2, starterCount = 1),
        PositionRequirement("ATA", idealCount = 4, starterCount = 1)
    )

    fun analyze(
        clubId: String,
        squad: List<AiSquadPlayer>,
        requirements: List<PositionRequirement> = defaultRequirements,
        targetOverall: Int = averageOverall(squad).coerceAtLeast(50)
    ): List<SquadNeed> = requirements.mapNotNull { requirement ->
        val players = squad.filter { samePosition(it.position, requirement.position) }
        val available = players.filter { it.injuredDays <= 14 && it.suspendedMatches <= 1 }
        val starterLevel = players.count { it.overall >= targetOverall }
        val expiring = players.count { it.contractMonthsRemaining <= 6 }
        val ageing = players.count { it.age >= 33 }

        val shortage = (requirement.idealCount - players.size).coerceAtLeast(0)
        val unavailableShortage = (requirement.starterCount - available.size).coerceAtLeast(0)
        val qualityShortage = (requirement.starterCount - starterLevel).coerceAtLeast(0)
        val risk = expiring + ageing

        val priority = when {
            players.isEmpty() || unavailableShortage >= requirement.starterCount -> SquadNeedPriority.URGENT
            shortage >= 2 || qualityShortage >= 2 -> SquadNeedPriority.HIGH
            shortage == 1 || qualityShortage == 1 || risk >= 2 -> SquadNeedPriority.MEDIUM
            risk == 1 -> SquadNeedPriority.LOW
            else -> null
        } ?: return@mapNotNull null

        SquadNeed(
            clubId = clubId,
            position = requirement.position,
            priority = priority,
            minimumOverall = minimumOverall(priority, targetOverall),
            maximumAge = maximumAge(priority)
        )
    }.sortedWith(
        compareByDescending<SquadNeed> { priorityRank(it.priority) }
            .thenBy { it.position }
    )

    private fun averageOverall(players: List<AiSquadPlayer>): Int =
        if (players.isEmpty()) 50 else players.map { it.overall }.average().toInt()

    private fun minimumOverall(priority: SquadNeedPriority, target: Int): Int = when (priority) {
        SquadNeedPriority.URGENT -> (target - 5).coerceAtLeast(40)
        SquadNeedPriority.HIGH -> (target - 2).coerceAtLeast(45)
        SquadNeedPriority.MEDIUM -> target.coerceAtLeast(50)
        SquadNeedPriority.LOW -> (target + 2).coerceAtMost(95)
    }

    private fun maximumAge(priority: SquadNeedPriority): Int? = when (priority) {
        SquadNeedPriority.URGENT -> null
        SquadNeedPriority.HIGH -> 32
        SquadNeedPriority.MEDIUM -> 29
        SquadNeedPriority.LOW -> 24
    }

    private fun priorityRank(priority: SquadNeedPriority): Int = when (priority) {
        SquadNeedPriority.LOW -> 1
        SquadNeedPriority.MEDIUM -> 2
        SquadNeedPriority.HIGH -> 3
        SquadNeedPriority.URGENT -> 4
    }

    private fun samePosition(first: String, second: String): Boolean =
        normalize(first) == normalize(second)

    private fun normalize(value: String): String = value.trim().uppercase()
}
