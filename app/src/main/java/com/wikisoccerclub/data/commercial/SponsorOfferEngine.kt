package com.wikisoccerclub.data.commercial

import kotlin.random.Random

object SponsorOfferEngine {

    fun generateOffers(
        clubId: String,
        clubName: String,
        clubReputation: Int,
        leagueReputation: Int,
        recentTitles: Int,
        seasonYear: Int,
        random: Random = Random.Default
    ): List<SponsorOffer> {
        require(clubReputation in 0..100)
        require(leagueReputation in 0..100)
        require(recentTitles >= 0)

        val amountBase =
            250_000L +
                clubReputation * 110_000L +
                leagueReputation * 55_000L +
                recentTitles * 900_000L

        val categories = listOf(
            SponsorCategory.MAIN_SHIRT,
            SponsorCategory.SLEEVE,
            SponsorCategory.KIT_SUPPLIER,
            SponsorCategory.REGIONAL_PARTNER
        )

        return categories.mapIndexed { index, category ->
            val categoryMultiplier =
                multiplierFor(category)
            val variation =
                random.nextDouble(0.85, 1.20)

            val annualValue = (
                amountBase *
                    categoryMultiplier *
                    variation
                ).toLong()

            val duration =
                random.nextInt(1, 4)

            SponsorOffer(
                id =
                    "oferta_${clubId}_${seasonYear}_${category.name}",
                sponsorName =
                    sponsorName(
                        clubName,
                        category,
                        index
                    ),
                category = category,
                durationSeasons = duration,
                fixedAnnualValue = annualValue,
                signingBonus =
                    (annualValue * 0.18).toLong(),
                reputationRequirement =
                    (clubReputation - 12)
                        .coerceAtLeast(0),
                exclusivityRequired =
                    category ==
                        SponsorCategory.MAIN_SHIRT ||
                        category ==
                            SponsorCategory.KIT_SUPPLIER,
                objectives =
                    defaultObjectives(
                        category,
                        clubReputation,
                        annualValue
                    )
            )
        }
    }

    fun canAccept(
        offer: SponsorOffer,
        clubReputation: Int,
        activeContracts: List<SponsorContract>
    ): Result<Unit> = runCatching {
        require(
            clubReputation >=
                offer.reputationRequirement
        ) {
            "A reputação do clube é insuficiente."
        }

        require(
            activeContracts.none {
                it.status == ContractStatus.ACTIVE &&
                    it.category == offer.category
            }
        ) {
            "Já existe um patrocinador ativo nesta categoria."
        }

        require(offer.durationSeasons in 1..10)
        require(offer.fixedAnnualValue >= 0)
        require(offer.signingBonus >= 0)
    }

    fun accept(
        offer: SponsorOffer,
        clubId: String,
        currentSeason: Int
    ): SponsorContract =
        SponsorContract(
            id = "contrato_${offer.id}",
            clubId = clubId,
            sponsorName = offer.sponsorName,
            category = offer.category,
            startSeason = currentSeason,
            endSeason =
                currentSeason +
                    offer.durationSeasons - 1,
            fixedAnnualValue =
                offer.fixedAnnualValue,
            signingBonus = offer.signingBonus,
            objectives = offer.objectives,
            totalReceived = offer.signingBonus
        )

    private fun multiplierFor(
        category: SponsorCategory
    ): Double = when (category) {
        SponsorCategory.MAIN_SHIRT -> 1.00
        SponsorCategory.SLEEVE -> 0.32
        SponsorCategory.KIT_SUPPLIER -> 0.55
        SponsorCategory.STADIUM_NAMING -> 0.85
        SponsorCategory.TRAINING_CENTER -> 0.40
        SponsorCategory.REGIONAL_PARTNER -> 0.22
    }

    private fun sponsorName(
        clubName: String,
        category: SponsorCategory,
        index: Int
    ): String {
        val names = listOf(
            "NovaBank",
            "SportMax",
            "Veloz Telecom",
            "Atlas Energia",
            "Prime Bet",
            "Global Tech"
        )

        return names[
            (clubName.length +
                category.ordinal +
                index) % names.size
        ]
    }

    private fun defaultObjectives(
        category: SponsorCategory,
        reputation: Int,
        annualValue: Long
    ): List<SponsorObjective> {
        val objective = when (category) {
            SponsorCategory.MAIN_SHIRT ->
                SponsorObjectiveType.LEAGUE_POSITION
            SponsorCategory.SLEEVE ->
                SponsorObjectiveType.AVERAGE_ATTENDANCE
            SponsorCategory.KIT_SUPPLIER ->
                SponsorObjectiveType.SOCIAL_GROWTH
            SponsorCategory.STADIUM_NAMING ->
                SponsorObjectiveType.AVERAGE_ATTENDANCE
            SponsorCategory.TRAINING_CENTER ->
                SponsorObjectiveType.USE_YOUTH_PLAYERS
            SponsorCategory.REGIONAL_PARTNER ->
                SponsorObjectiveType.REACH_KNOCKOUT_STAGE
        }

        val target = when (objective) {
            SponsorObjectiveType.LEAGUE_POSITION ->
                (12 - reputation / 12)
                    .coerceIn(1, 12)
            SponsorObjectiveType.AVERAGE_ATTENDANCE ->
                10_000 + reputation * 350
            SponsorObjectiveType.SOCIAL_GROWTH ->
                5 + reputation / 10
            SponsorObjectiveType.USE_YOUTH_PLAYERS -> 8
            SponsorObjectiveType.REACH_KNOCKOUT_STAGE -> 1
            SponsorObjectiveType.WIN_COMPETITION -> 1
            SponsorObjectiveType.SIGN_REPUTABLE_PLAYER -> 1
        }

        return listOf(
            SponsorObjective(
                type = objective,
                targetValue = target,
                bonusValue =
                    (annualValue * 0.15).toLong(),
                penaltyValue =
                    (annualValue * 0.08).toLong()
            )
        )
    }
}
