package com.wikisoccerclub.data.transfer

object LoanEngine {

    fun validate(offer: LoanOffer): Result<Unit> = runCatching {
        require(offer.playerId.isNotBlank()) {
            "O jogador precisa ser informado."
        }
        require(offer.ownerClubId != offer.destinationClubId) {
            "O clube proprietário e o clube de destino devem ser diferentes."
        }
        require(offer.endYear >= offer.startYear) {
            "O término do empréstimo não pode ser anterior ao início."
        }
        require(
            offer.wagePercentagePaidByDestination in 0..100
        ) {
            "O percentual de salário deve ficar entre 0 e 100."
        }
        require(offer.monthlyFee >= 0) {
            "A taxa mensal não pode ser negativa."
        }
        require(offer.purchaseOption == null ||
            offer.purchaseOption >= 0
        ) {
            "A opção de compra não pode ser negativa."
        }
    }

    fun accept(offer: LoanOffer): Pair<LoanOffer, ActiveLoan> {
        validate(offer).getOrThrow()

        val accepted = offer.copy(status = LoanStatus.ACTIVE)
        val active = ActiveLoan(
            offerId = accepted.id,
            playerId = accepted.playerId,
            ownerClubId = accepted.ownerClubId,
            destinationClubId = accepted.destinationClubId,
            endYear = accepted.endYear,
            purchaseOption = accepted.purchaseOption
        )

        return accepted to active
    }

    fun reject(offer: LoanOffer): LoanOffer =
        offer.copy(status = LoanStatus.REJECTED)

    fun complete(offer: LoanOffer): LoanOffer =
        offer.copy(status = LoanStatus.COMPLETED)

    fun activatePurchaseOption(
        offer: LoanOffer,
        paidValue: Long
    ): LoanOffer {
        val option = offer.purchaseOption
            ?: error("Este empréstimo não possui opção de compra.")

        require(offer.status == LoanStatus.ACTIVE) {
            "O empréstimo precisa estar ativo."
        }
        require(paidValue >= option) {
            "O valor pago é inferior à opção de compra."
        }

        return offer.copy(status = LoanStatus.PURCHASED)
    }

    fun expiredLoans(
        loans: List<ActiveLoan>,
        currentYear: Int
    ): List<ActiveLoan> =
        loans.filter { currentYear > it.endYear }
}
