package com.wikisoccerclub.data.transfer

object ContractEngine {

    fun validate(offer: ContractOffer): Result<Unit> = runCatching {
        require(offer.endYear > offer.startYear) {
            "O contrato precisa durar pelo menos uma temporada."
        }
        require(offer.monthlySalary > 0) {
            "O salário precisa ser maior que zero."
        }
        require(offer.signingBonus >= 0) {
            "O bônus de assinatura não pode ser negativo."
        }
        require(offer.releaseClause == null ||
            offer.releaseClause > 0
        ) {
            "A multa rescisória precisa ser positiva."
        }
    }

    fun accept(
        offer: ContractOffer
    ): Pair<ContractOffer, PlayerContract> {
        validate(offer).getOrThrow()

        val accepted = offer.copy(
            status = ContractNegotiationStatus.ACCEPTED
        )

        val contract = PlayerContract(
            playerId = accepted.playerId,
            clubId = accepted.clubId,
            startYear = accepted.startYear,
            endYear = accepted.endYear,
            monthlySalary = accepted.monthlySalary,
            releaseClause = accepted.releaseClause
        )

        return accepted to contract
    }

    fun reject(offer: ContractOffer): ContractOffer =
        offer.copy(
            status = ContractNegotiationStatus.REJECTED
        )

    fun counter(
        offer: ContractOffer,
        monthlySalary: Long,
        endYear: Int,
        signingBonus: Long = offer.signingBonus
    ): ContractOffer {
        val counter = offer.copy(
            monthlySalary = monthlySalary,
            endYear = endYear,
            signingBonus = signingBonus,
            status = ContractNegotiationStatus.COUNTER
        )
        validate(counter).getOrThrow()
        return counter
    }

    fun isExpired(
        contract: PlayerContract,
        currentYear: Int
    ): Boolean = currentYear > contract.endYear
}
