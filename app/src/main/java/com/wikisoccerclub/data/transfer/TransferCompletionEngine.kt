package com.wikisoccerclub.data.transfer

object TransferCompletionEngine {

    fun completePermanentTransfer(
        transferId: String,
        playerId: String,
        sellingClubId: String?,
        buyingClubId: String,
        transferValue: Long,
        contract: PlayerContract,
        seasonYear: Int,
        clubs: List<ClubTransferState>
    ): TransferCompletionResult {
        return runCatching {
            require(playerId.isNotBlank()) {
                "O jogador precisa ser informado."
            }
            require(buyingClubId.isNotBlank()) {
                "O clube comprador precisa ser informado."
            }
            require(transferValue >= 0) {
                "O valor da transferência não pode ser negativo."
            }
            require(contract.playerId == playerId) {
                "O contrato pertence a outro jogador."
            }
            require(contract.clubId == buyingClubId) {
                "O contrato pertence a outro clube."
            }
            require(sellingClubId == null ||
                sellingClubId != buyingClubId
            ) {
                "O clube comprador e o vendedor devem ser diferentes."
            }

            val clubMap = clubs.associateBy {
                it.clubId
            }.toMutableMap()

            val buyer = clubMap[buyingClubId]
                ?: error("Clube comprador não encontrado.")

            require(buyer.balance >= transferValue) {
                "O clube comprador não possui saldo suficiente."
            }
            require(playerId !in buyer.playerIds) {
                "O jogador já pertence ao clube comprador."
            }

            val seller = sellingClubId?.let { sellerId ->
                clubMap[sellerId]
                    ?: error("Clube vendedor não encontrado.")
            }

            if (seller != null) {
                require(playerId in seller.playerIds) {
                    "O jogador não pertence ao clube vendedor."
                }

                clubMap[seller.clubId] = seller.copy(
                    balance = seller.balance + transferValue,
                    playerIds = seller.playerIds - playerId
                )
            }

            clubMap[buyer.clubId] = buyer.copy(
                balance = buyer.balance - transferValue,
                playerIds = (buyer.playerIds + playerId).distinct()
            )

            val transfer = CompletedTransfer(
                id = transferId,
                playerId = playerId,
                sellingClubId = sellingClubId,
                buyingClubId = buyingClubId,
                transferValue = transferValue,
                contract = contract,
                completedAtSeasonYear = seasonYear
            )

            TransferCompletionResult(
                transfer = transfer,
                updatedClubs = clubMap.values.toList()
            )
        }.getOrElse { error ->
            TransferCompletionResult(
                transfer = null,
                updatedClubs = clubs,
                error = error.message ?: "Não foi possível concluir a transferência."
            )
        }
    }

    fun completeLoanPurchase(
        loanOffer: LoanOffer,
        contract: PlayerContract,
        seasonYear: Int,
        clubs: List<ClubTransferState>
    ): TransferCompletionResult {
        val purchaseValue = loanOffer.purchaseOption
            ?: return TransferCompletionResult(
                transfer = null,
                updatedClubs = clubs,
                error = "O empréstimo não possui opção de compra."
            )

        require(loanOffer.status == LoanStatus.ACTIVE) {
            "O empréstimo precisa estar ativo."
        }

        return completePermanentTransfer(
            transferId = "compra_${loanOffer.id}",
            playerId = loanOffer.playerId,
            sellingClubId = loanOffer.ownerClubId,
            buyingClubId = loanOffer.destinationClubId,
            transferValue = purchaseValue,
            contract = contract,
            seasonYear = seasonYear,
            clubs = clubs
        )
    }
}
