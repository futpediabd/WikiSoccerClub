package com.wikisoccerclub.data.transfer

enum class TransferNewsCategory {
    OFFER,
    NEGOTIATION,
    SIGNING,
    SALE,
    LOAN,
    CONTRACT,
    YOUTH_PROMOTION
}

data class TransferNewsItem(
    val id: String,
    val seasonYear: Int,
    val title: String,
    val body: String,
    val category: TransferNewsCategory,
    val playerId: String? = null,
    val clubIds: List<String> = emptyList(),
    val transferValue: Long? = null,
    val important: Boolean = false
)

enum class TransferAuditType {
    OFFER_CREATED,
    OFFER_ACCEPTED,
    OFFER_REJECTED,
    COUNTER_OFFERED,
    TRANSFER_COMPLETED,
    CONTRACT_RENEWED,
    YOUTH_PROMOTED,
    PLAYER_LISTED
}

data class TransferAuditEvent(
    val id: String,
    val seasonYear: Int,
    val type: TransferAuditType,
    val playerId: String? = null,
    val clubId: String? = null,
    val relatedClubId: String? = null,
    val value: Long? = null,
    val description: String
)
