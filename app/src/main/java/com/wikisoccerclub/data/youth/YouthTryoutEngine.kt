package com.wikisoccerclub.data.youth

import kotlin.random.Random

object YouthTryoutEngine {

    fun runTryout(
        config: YouthTryoutConfig,
        nationalityPool: List<String>,
        firstNames: List<String>,
        lastNames: List<String>,
        currentDay: Int,
        random: Random = Random.Default
    ): YouthTryoutResult {
        require(config.minimumAge in 15..21)
        require(config.maximumAge in config.minimumAge..21)
        require(config.candidateCount in 1..20)
        require(config.localNationalityChance in 0..100)
        require(config.cost >= 0L)

        val validNationalities =
            nationalityPool
                .filter { it.isNotBlank() }
                .distinct()
                .ifEmpty {
                    listOf(config.clubCountry)
                }

        val candidates =
            List(config.candidateCount) { index ->
                generateCandidate(
                    config = config,
                    nationalityPool = validNationalities,
                    firstNames = firstNames,
                    lastNames = lastNames,
                    index = index,
                    currentDay = currentDay,
                    random = random
                )
            }

        return YouthTryoutResult(
            id =
                "peneira_${config.clubId}_$currentDay",
            clubId = config.clubId,
            positionFilter =
                config.positionFilter,
            candidates = candidates,
            cost = config.cost,
            generatedDay = currentDay
        )
    }

    fun signCandidate(
        candidate: YouthCandidate,
        currentSquadSize: Int,
        maximumSquadSize: Int = 40
    ): YouthCandidate {
        require(
            candidate.status ==
                YouthPlayerStatus.AVAILABLE
        ) {
            "O jogador não está mais disponível."
        }

        require(currentSquadSize < maximumSquadSize) {
            "O elenco atingiu o limite de jogadores."
        }

        return candidate.copy(
            status = YouthPlayerStatus.SIGNED
        )
    }

    fun rejectCandidate(
        candidate: YouthCandidate
    ): YouthCandidate {
        require(
            candidate.status ==
                YouthPlayerStatus.AVAILABLE
        ) {
            "O jogador não está mais disponível."
        }

        return candidate.copy(
            status = YouthPlayerStatus.REJECTED
        )
    }

    private fun generateCandidate(
        config: YouthTryoutConfig,
        nationalityPool: List<String>,
        firstNames: List<String>,
        lastNames: List<String>,
        index: Int,
        currentDay: Int,
        random: Random
    ): YouthCandidate {
        val nationality =
            chooseNationality(
                clubCountry =
                    config.clubCountry,
                nationalityPool =
                    nationalityPool,
                localChance =
                    config.localNationalityChance,
                random = random
            )

        val group =
            if (
                config.positionFilter ==
                YouthPositionFilter.ALL
            ) {
                randomPositionGroup(random)
            } else {
                config.positionFilter
            }

        val specificPosition =
            specificPosition(
                group = group,
                random = random
            )

        val age =
            random.nextInt(
                config.minimumAge,
                config.maximumAge + 1
            )

        val overallBase =
            30 + ((age - 15) * 3)

        val overall =
            random.nextInt(
                overallBase.coerceAtMost(58),
                (overallBase + 18)
                    .coerceAtMost(76) + 1
            ).coerceIn(30, 76)

        val potentialMinimum =
            (overall + 4).coerceAtMost(96)

        val potential =
            random.nextInt(
                potentialMinimum,
                101
            ).coerceIn(overall, 100)

        val salary =
            (
                800L +
                    overall * 45L +
                    potential * 25L
                )

        return YouthCandidate(
            id =
                "jovem_${config.clubId}_" +
                    "${currentDay}_$index",
            name =
                generateName(
                    firstNames = firstNames,
                    lastNames = lastNames,
                    index = index,
                    random = random
                ),
            nationality = nationality,
            age = age,
            position = group,
            specificPosition =
                specificPosition,
            overall = overall,
            potential = potential,
            salaryRequest = salary
        )
    }

    private fun chooseNationality(
        clubCountry: String,
        nationalityPool: List<String>,
        localChance: Int,
        random: Random
    ): String {
        val useLocal =
            random.nextInt(100) < localChance

        if (useLocal) {
            return clubCountry
        }

        return nationalityPool
            .filterNot {
                it.equals(
                    clubCountry,
                    ignoreCase = true
                )
            }
            .ifEmpty {
                nationalityPool
            }
            .random(random)
    }

    private fun randomPositionGroup(
        random: Random
    ): YouthPositionFilter {
        return when (random.nextInt(100)) {
            in 0..11 ->
                YouthPositionFilter.GOALKEEPER
            in 12..39 ->
                YouthPositionFilter.DEFENDER
            in 40..69 ->
                YouthPositionFilter.MIDFIELDER
            else ->
                YouthPositionFilter.ATTACKER
        }
    }

    private fun specificPosition(
        group: YouthPositionFilter,
        random: Random
    ): String {
        return when (group) {
            YouthPositionFilter.GOALKEEPER ->
                "Goleiro"

            YouthPositionFilter.DEFENDER ->
                listOf(
                    "Lateral-direito",
                    "Lateral-esquerdo",
                    "Zagueiro"
                ).random(random)

            YouthPositionFilter.MIDFIELDER ->
                listOf(
                    "Volante",
                    "Meio-campista",
                    "Meia ofensivo"
                ).random(random)

            YouthPositionFilter.ATTACKER ->
                listOf(
                    "Ponta-direita",
                    "Ponta-esquerda",
                    "Centroavante"
                ).random(random)

            YouthPositionFilter.ALL ->
                specificPosition(
                    randomPositionGroup(random),
                    random
                )
        }
    }

    private fun generateName(
        firstNames: List<String>,
        lastNames: List<String>,
        index: Int,
        random: Random
    ): String {
        val first =
            firstNames
                .filter { it.isNotBlank() }
                .randomOrNull(random)
                ?: "Jogador"

        val last =
            lastNames
                .filter { it.isNotBlank() }
                .randomOrNull(random)
                ?: "Jovem ${index + 1}"

        return "$first $last"
    }
}
