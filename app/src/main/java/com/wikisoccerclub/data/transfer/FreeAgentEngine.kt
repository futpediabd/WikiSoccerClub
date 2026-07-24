package com.wikisoccerclub.data.transfer

object FreeAgentEngine {

    fun createContract(
        request: FreeAgentSigningRequest
    ): PlayerContract {
        require(request.clubId.isNotBlank()) {
            "O clube precisa ser informado."
        }
        require(request.monthlySalary >=
            request.freeAgent.minimumSalary
        ) {
            "O salário oferecido está abaixo do mínimo esperado."
        }
        require(request.contractYears in 1..5) {
            "O contrato deve ter entre 1 e 5 temporadas."
        }
        require(request.signingBonus >= 0) {
            "O bônus de assinatura não pode ser negativo."
        }

        return PlayerContract(
            playerId = request.freeAgent.playerId,
            clubId = request.clubId,
            startYear = request.seasonYear,
            endYear = request.seasonYear +
                request.contractYears,
            monthlySalary = request.monthlySalary
        )
    }

    fun sign(
        request: FreeAgentSigningRequest,
        clubs: List<ClubTransferState>
    ): TransferCompletionResult {
        val contract = runCatching {
            createContract(request)
        }.getOrElse {
            return TransferCompletionResult(
                transfer = null,
                updatedClubs = clubs,
                error = it.message
            )
        }

        val buyer = clubs.firstOrNull {
            it.clubId == request.clubId
        } ?: return TransferCompletionResult(
            transfer = null,
            updatedClubs = clubs,
            error = "Clube não encontrado."
        )

        if (buyer.balance < request.signingBonus) {
            return TransferCompletionResult(
                transfer = null,
                updatedClubs = clubs,
                error = "Saldo insuficiente para pagar o bônus."
            )
        }

        val updatedClubs = clubs.map { club ->
            if (club.clubId == buyer.clubId) {
                club.copy(
                    balance = club.balance -
                        request.signingBonus,
                    playerIds = (
                        club.playerIds +
                            request.freeAgent.playerId
                        ).distinct()
                )
            } else {
                club
            }
        }

        return TransferCompletionResult(
            transfer = CompletedTransfer(
                id = request.id,
                playerId = request.freeAgent.playerId,
                sellingClubId = null,
                buyingClubId = request.clubId,
                transferValue = request.signingBonus,
                contract = contract,
                completedAtSeasonYear = request.seasonYear
            ),
            updatedClubs = updatedClubs
        )
    }
}
