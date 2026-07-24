package com.wikisoccerclub.data.finance

object SponsorshipEngine {

    fun validateOffer(
        offer: SponsorshipOffer
    ): Result<Unit> = runCatching {
        require(offer.clubId.isNotBlank()) {
            "O clube precisa ser informado."
        }
        require(offer.sponsorName.isNotBlank()) {
            "O patrocinador precisa ser informado."
        }
        require(offer.durationYears in 1..10) {
            "O contrato deve durar entre 1 e 10 temporadas."
        }
        require(offer.signingBonus >= 0) {
            "O bônus de assinatura não pode ser negativo."
        }
        require(offer.monthlyPayment >= 0) {
            "O pagamento mensal não pode ser negativo."
        }
        require(offer.championshipBonus >= 0) {
            "O bônus por título não pode ser negativo."
        }
    }

    fun accept(
        offer: SponsorshipOffer
    ): Pair<SponsorshipOffer, ActiveSponsorship> {
        validateOffer(offer).getOrThrow()

        val accepted = offer.copy(
            status = SponsorshipStatus.ACTIVE
        )

        return accepted to ActiveSponsorship(
            offerId = accepted.id,
            clubId = accepted.clubId,
            sponsorName = accepted.sponsorName,
            startYear = accepted.startYear,
            endYear = accepted.startYear +
                accepted.durationYears - 1,
            monthlyPayment = accepted.monthlyPayment,
            championshipBonus =
                accepted.championshipBonus,
            objectiveDescription =
                accepted.objectiveDescription
        )
    }

    fun reject(
        offer: SponsorshipOffer
    ): SponsorshipOffer =
        offer.copy(
            status = SponsorshipStatus.REJECTED
        )

    fun monthlyTransaction(
        sponsorship: ActiveSponsorship,
        seasonYear: Int,
        month: Int
    ): FinanceTransaction {
        require(
            seasonYear in sponsorship.startYear..
                sponsorship.endYear
        ) {
            "O patrocínio não está ativo nesta temporada."
        }

        return FinanceTransaction(
            id = "pat_${sponsorship.offerId}_${seasonYear}_$month",
            clubId = sponsorship.clubId,
            seasonYear = seasonYear,
            month = month,
            type = FinanceTransactionType.SPONSORSHIP,
            description = "Patrocínio: ${sponsorship.sponsorName}",
            amount = sponsorship.monthlyPayment
        )
    }

    fun championshipBonusTransaction(
        sponsorship: ActiveSponsorship,
        seasonYear: Int,
        month: Int,
        competitionName: String
    ): FinanceTransaction =
        FinanceTransaction(
            id = "bonus_${sponsorship.offerId}_${seasonYear}_$competitionName",
            clubId = sponsorship.clubId,
            seasonYear = seasonYear,
            month = month,
            type = FinanceTransactionType.SPONSORSHIP,
            description = "Bônus por título: $competitionName",
            amount = sponsorship.championshipBonus
        )

    fun isExpired(
        sponsorship: ActiveSponsorship,
        currentYear: Int
    ): Boolean = currentYear > sponsorship.endYear
}
