package com.wikisoccerclub.core.transfer

import com.wikisoccerclub.data.transfer.*

/** Fluxo real de compra usando apenas as classes existentes no projeto. */
class TransferWorkflowService(
    private val offerRepository: TransferOfferRepository,
    private val contractRepository: ContractRepository,
    private val clubRepository: ClubTransferRepository,
    private val historyRepository: TransferHistoryRepository,
    private val windowRepository: TransferWindowRepository,
    private val integrationService: TransferIntegrationService
) {
    fun registerClubs(clubs: List<ClubTransferState>) {
        clubRepository.saveAll(clubs)
    }

    fun createOffer(offer: TransferOffer, seasonYear: Int = 0): Result<TransferOffer> = runCatching {
        windowRepository.requireOpen()
        require(offer.id.isNotBlank()) { "Identificador da proposta não informado." }
        require(offer.playerId.isNotBlank()) { "Jogador não informado." }
        require(offer.buyingClubId.isNotBlank()) { "Clube comprador não informado." }
        require(offer.buyingClubId != offer.sellingClubId) {
            "Comprador e vendedor precisam ser diferentes."
        }
        require(offer.value >= 0L) { "Valor da proposta inválido." }
        val pending = offer.copy(status = OfferStatus.PENDING)
        offerRepository.save(pending)
        integrationService.recordOffer(
            pending, seasonYear, TransferAuditType.OFFER_CREATED,
            "Proposta criada por ${pending.buyingClubId}."
        )
        pending
    }

    fun acceptOffer(offerId: String, seasonYear: Int = 0): Result<TransferOffer> =
        changeOffer(offerId) { TransferOfferEngine.accept(it) }.onSuccess {
            integrationService.recordOffer(
                it, seasonYear, TransferAuditType.OFFER_ACCEPTED,
                "A proposta de ${it.buyingClubId} foi aceita."
            )
        }

    fun rejectOffer(offerId: String, seasonYear: Int = 0): Result<TransferOffer> =
        changeOffer(offerId) { TransferOfferEngine.reject(it) }.onSuccess {
            integrationService.recordOffer(
                it, seasonYear, TransferAuditType.OFFER_REJECTED,
                "A proposta de ${it.buyingClubId} foi recusada."
            )
        }

    fun counterOffer(offerId: String, newValue: Long, seasonYear: Int = 0): Result<TransferOffer> = runCatching {
        windowRepository.requireOpen()
        require(newValue >= 0L) { "Valor da contraproposta inválido." }
        val offer = findOffer(offerId)
        TransferOfferEngine.counter(offer, newValue).also {
            offerRepository.save(it)
            integrationService.recordOffer(
                it, seasonYear, TransferAuditType.COUNTER_OFFERED,
                "Uma contraproposta foi enviada no valor de ${it.value}."
            )
        }
    }

    fun completeTransfer(
        offerId: String,
        seasonYear: Int
    ): TransferCompletionResult {
        if (!windowRepository.isOpen()) return failure("A janela de transferências está fechada.")
        val offer = offerRepository.all().firstOrNull { it.id == offerId }
            ?: return failure("Proposta não encontrada.")
        if (offer.status != OfferStatus.ACCEPTED) {
            return failure("A proposta precisa estar aceita.")
        }
        val contract = contractRepository.findContract(offer.playerId)
            ?: return failure("O jogador precisa assinar o contrato antes da transferência.")
        if (contract.clubId != offer.buyingClubId) {
            return failure("O contrato pertence a outro clube.")
        }

        val result = TransferCompletionEngine.completePermanentTransfer(
            transferId = offer.id,
            playerId = offer.playerId,
            sellingClubId = offer.sellingClubId.takeIf(String::isNotBlank),
            buyingClubId = offer.buyingClubId,
            transferValue = offer.value,
            contract = contract,
            seasonYear = seasonYear,
            clubs = clubRepository.all()
        )
        if (result.successful) {
            clubRepository.saveAll(result.updatedClubs)
            result.transfer?.let {
                historyRepository.save(it)
                integrationService.recordCompletion(it)
            }
        }
        return result
    }

    private fun changeOffer(
        offerId: String,
        transform: (TransferOffer) -> TransferOffer
    ): Result<TransferOffer> = runCatching {
        transform(findOffer(offerId)).also(offerRepository::save)
    }

    private fun findOffer(id: String): TransferOffer =
        offerRepository.all().firstOrNull { it.id == id }
            ?: error("Proposta não encontrada.")

    private fun failure(message: String) = TransferCompletionResult(
        transfer = null,
        updatedClubs = clubRepository.all(),
        error = message
    )
}
