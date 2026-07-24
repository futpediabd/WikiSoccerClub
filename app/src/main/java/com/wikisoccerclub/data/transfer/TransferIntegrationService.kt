package com.wikisoccerclub.data.transfer

import com.wikisoccerclub.data.finance.ClubFinance
import com.wikisoccerclub.data.finance.FinanceEngine
import com.wikisoccerclub.data.finance.FinanceRepository
import com.wikisoccerclub.data.finance.FinanceTransaction
import com.wikisoccerclub.data.finance.FinanceTransactionType

/**
 * Finaliza os efeitos transversais do mercado em notícias, auditoria e finanças.
 * O elenco já é atualizado pelo TransferCompletionEngine; este serviço mantém
 * os demais módulos consistentes e evita registros duplicados pelo id.
 */
class TransferIntegrationService(
    private val financeRepository: FinanceRepository,
    private val newsRepository: TransferNewsRepository
) {
    fun recordOffer(
        offer: TransferOffer,
        seasonYear: Int,
        type: TransferAuditType,
        description: String
    ) {
        newsRepository.saveAudit(
            TransferAuditEvent(
                id = "audit_${type.name.lowercase()}_${offer.id}",
                seasonYear = seasonYear,
                type = type,
                playerId = offer.playerId,
                clubId = offer.buyingClubId,
                relatedClubId = offer.sellingClubId.takeIf(String::isNotBlank),
                value = offer.value,
                description = description
            )
        )

        newsRepository.saveNews(
            TransferNewsItem(
                id = "news_${type.name.lowercase()}_${offer.id}",
                seasonYear = seasonYear,
                title = when (type) {
                    TransferAuditType.OFFER_ACCEPTED -> "Proposta aceita"
                    TransferAuditType.OFFER_REJECTED -> "Proposta recusada"
                    TransferAuditType.COUNTER_OFFERED -> "Contraproposta enviada"
                    else -> "Nova proposta no mercado"
                },
                body = description,
                category = TransferNewsCategory.NEGOTIATION,
                playerId = offer.playerId,
                clubIds = listOf(offer.buyingClubId, offer.sellingClubId)
                    .filter(String::isNotBlank),
                transferValue = offer.value
            )
        )
    }

    fun recordCompletion(transfer: CompletedTransfer, month: Int = 1) {
        val sellerId = transfer.sellingClubId
        val buyerId = transfer.buyingClubId

        registerFinanceTransaction(
            clubId = buyerId,
            transaction = FinanceTransaction(
                id = "transfer_buy_${transfer.id}",
                clubId = buyerId,
                seasonYear = transfer.completedAtSeasonYear,
                month = month.coerceIn(1, 12),
                type = FinanceTransactionType.PLAYER_PURCHASE,
                description = "Compra do jogador ${transfer.playerId}",
                amount = -transfer.transferValue
            )
        )

        if (sellerId != null) {
            registerFinanceTransaction(
                clubId = sellerId,
                transaction = FinanceTransaction(
                    id = "transfer_sale_${transfer.id}",
                    clubId = sellerId,
                    seasonYear = transfer.completedAtSeasonYear,
                    month = month.coerceIn(1, 12),
                    type = FinanceTransactionType.PLAYER_SALE,
                    description = "Venda do jogador ${transfer.playerId}",
                    amount = transfer.transferValue
                )
            )
        }

        val clubs = listOfNotNull(sellerId, buyerId)
        newsRepository.saveNews(
            TransferNewsItem(
                id = "news_completed_${transfer.id}",
                seasonYear = transfer.completedAtSeasonYear,
                title = "Transferência concluída",
                body = "O jogador ${transfer.playerId} foi contratado por $buyerId.",
                category = TransferNewsCategory.SIGNING,
                playerId = transfer.playerId,
                clubIds = clubs,
                transferValue = transfer.transferValue,
                important = true
            )
        )
        newsRepository.saveAudit(
            TransferAuditEvent(
                id = "audit_completed_${transfer.id}",
                seasonYear = transfer.completedAtSeasonYear,
                type = TransferAuditType.TRANSFER_COMPLETED,
                playerId = transfer.playerId,
                clubId = buyerId,
                relatedClubId = sellerId,
                value = transfer.transferValue,
                description = "Transferência concluída e integrada ao elenco e às finanças."
            )
        )
    }

    private fun registerFinanceTransaction(
        clubId: String,
        transaction: FinanceTransaction
    ) {
        val current = financeRepository.findFinance(clubId)
            ?: ClubFinance(
                clubId = clubId,
                balance = 0L,
                transferBudget = Long.MAX_VALUE
            )
        val updated = FinanceEngine.applyTransaction(current, transaction)
        financeRepository.saveTransaction(transaction)
        financeRepository.saveFinance(updated)
    }
}
