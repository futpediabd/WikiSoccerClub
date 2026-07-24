package com.wikisoccerclub.data.transfer

import kotlin.math.roundToLong

object TransferAiNegotiationEngine {

    fun evaluateIncomingOffer(
        offer: TransferOffer,
        player: MarketPlayer,
        sellingClubBalance: Long,
        replacementCost: Long = player.marketValue
    ): TransferOffer {
        require(
            offer.playerId == player.playerId
        ) {
            "A proposta pertence a outro jogador."
        }

        val minimumAccepted = minimumAcceptedValue(
            player = player,
            sellingClubBalance = sellingClubBalance,
            replacementCost = replacementCost
        )

        return when {
            offer.value >= minimumAccepted ->
                TransferOfferEngine.accept(offer)

            offer.value >=
                (minimumAccepted * 0.85).roundToLong() ->
                TransferOfferEngine.counter(
                    offer = offer,
                    newValue = minimumAccepted
                )

            else -> TransferOfferEngine.reject(offer)
        }
    }

    fun minimumAcceptedValue(
        player: MarketPlayer,
        sellingClubBalance: Long,
        replacementCost: Long
    ): Long {
        if (player.clubId == null) return 0

        val saleMultiplier = when {
            player.listedForSale -> 0.90
            player.age >= 33 -> 0.92
            player.potential >= player.overall + 8 -> 1.25
            player.overall >= 85 -> 1.20
            else -> 1.05
        }

        val financialAdjustment =
            if (sellingClubBalance < replacementCost) {
                0.95
            } else {
                1.05
            }

        return (
            player.marketValue *
                saleMultiplier *
                financialAdjustment
            ).roundToLong()
            .coerceAtLeast(0)
    }
}
