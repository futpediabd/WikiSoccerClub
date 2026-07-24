package com.wikisoccerclub.data.scouting

import kotlin.math.abs
import kotlin.random.Random

object ScoutingEngine {

    fun startAssignment(
        assignment: ScoutingAssignment,
        currentDay: Int
    ): ScoutingAssignment {
        require(assignment.minimumAge in 14..50)
        require(assignment.maximumAge in assignment.minimumAge..50)
        require(assignment.minimumPotential in 0..100)
        require(assignment.durationDays in 1..365)

        return assignment.copy(
            startDay = currentDay,
            status = ScoutingStatus.IN_PROGRESS
        )
    }

    fun canComplete(
        assignment: ScoutingAssignment,
        currentDay: Int
    ): Boolean =
        assignment.status == ScoutingStatus.IN_PROGRESS &&
            currentDay >= assignment.startDay + assignment.durationDays

    fun completeAssignment(
        assignment: ScoutingAssignment,
        currentDay: Int
    ): ScoutingAssignment {
        require(canComplete(assignment, currentDay)) {
            "A observação ainda não foi concluída."
        }

        return assignment.copy(
            status = ScoutingStatus.COMPLETED
        )
    }

    fun generateReports(
        scout: ScoutProfile,
        assignment: ScoutingAssignment,
        candidates: List<ScoutedPlayer>,
        currentDay: Int,
        random: Random = Random.Default
    ): List<ScoutingReport> {
        require(assignment.status == ScoutingStatus.COMPLETED)

        val knowledge =
            scout.regionKnowledge[assignment.region] ?: 0

        return candidates
            .filter {
                it.age in
                    assignment.minimumAge..assignment.maximumAge
            }
            .filter {
                assignment.positionGroup ==
                    PlayerPositionGroup.ANY ||
                    it.positionGroup ==
                        assignment.positionGroup
            }
            .filter {
                it.potential >=
                    assignment.minimumPotential
            }
            .map { player ->
                buildReport(
                    scout = scout,
                    assignment = assignment,
                    player = player,
                    regionalKnowledge = knowledge,
                    currentDay = currentDay,
                    random = random
                )
            }
            .sortedByDescending {
                it.recommendationScore
            }
    }

    private fun buildReport(
        scout: ScoutProfile,
        assignment: ScoutingAssignment,
        player: ScoutedPlayer,
        regionalKnowledge: Int,
        currentDay: Int,
        random: Random
    ): ScoutingReport {
        val confidence = (
            scout.ability * 0.45 +
                scout.potentialJudgement * 0.35 +
                regionalKnowledge * 0.20 +
                assignment.durationDays.coerceAtMost(60) / 3.0
            ).toInt().coerceIn(20, 100)

        val errorMargin =
            ((100 - confidence) / 8)
                .coerceAtLeast(1)

        val observedCurrent = (
            player.currentAbility +
                random.nextInt(
                    -errorMargin,
                    errorMargin + 1
                )
            ).coerceIn(0, 100)

        val observedPotential = (
            player.potential +
                random.nextInt(
                    -errorMargin,
                    errorMargin + 1
                )
            ).coerceIn(observedCurrent, 100)

        val valueVariation =
            random.nextDouble(
                0.90 - (100 - confidence) / 500.0,
                1.10 + (100 - confidence) / 500.0
            )

        val estimatedValue =
            (player.marketValue * valueVariation)
                .toLong()
                .coerceAtLeast(0)

        val estimatedWage =
            (
                player.wageDemand *
                    random.nextDouble(0.90, 1.15)
                ).toLong()

        val ageScore = when {
            player.age <= 20 -> 12
            player.age <= 24 -> 8
            player.age <= 29 -> 4
            player.age <= 33 -> 0
            else -> -8
        }

        val contractScore = when {
            player.contractMonthsRemaining <= 6 -> 10
            player.contractMonthsRemaining <= 12 -> 6
            else -> 0
        }

        val interestScore =
            if (player.interestedInMove) 8 else -6

        val recommendation = (
            observedCurrent * 0.35 +
                observedPotential * 0.40 +
                ageScore +
                contractScore +
                interestScore
            ).toInt().coerceIn(0, 100)

        return ScoutingReport(
            id =
                "relatorio_${assignment.id}_${player.playerId}",
            assignmentId = assignment.id,
            scoutId = scout.id,
            playerId = player.playerId,
            observedCurrentAbility =
                observedCurrent,
            observedPotential =
                observedPotential,
            confidence = confidence,
            estimatedMarketValue =
                estimatedValue,
            estimatedWageDemand =
                estimatedWage,
            recommendationScore =
                recommendation,
            strengths =
                buildStrengths(player),
            weaknesses =
                buildWeaknesses(player),
            generatedDay = currentDay
        )
    }

    private fun buildStrengths(
        player: ScoutedPlayer
    ): List<String> {
        val strengths = mutableListOf<String>()

        if (player.potential >= 85) {
            strengths += "Potencial elevado"
        }
        if (player.currentAbility >= 80) {
            strengths += "Pronto para atuar"
        }
        if (player.age <= 21) {
            strengths += "Boa margem de evolução"
        }
        if (player.contractMonthsRemaining <= 6) {
            strengths += "Contrato próximo do fim"
        }
        if (player.interestedInMove) {
            strengths += "Interesse em transferência"
        }

        return if (strengths.isEmpty()) {
            listOf("Perfil equilibrado")
        } else strengths
    }

    private fun buildWeaknesses(
        player: ScoutedPlayer
    ): List<String> {
        val weaknesses = mutableListOf<String>()

        if (player.age >= 33) {
            weaknesses += "Pouca margem de evolução"
        }
        if (player.wageDemand >
            player.marketValue / 40
        ) {
            weaknesses += "Exigência salarial elevada"
        }
        if (!player.interestedInMove) {
            weaknesses += "Baixo interesse em transferência"
        }
        if (abs(player.potential -
                player.currentAbility) < 5
        ) {
            weaknesses += "Próximo do limite técnico"
        }

        return if (weaknesses.isEmpty()) {
            listOf("Nenhuma fraqueza relevante")
        } else weaknesses
    }
}
