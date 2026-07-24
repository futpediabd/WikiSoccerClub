package com.wikisoccerclub.data.transfer

enum class TransferCompletionStatus {
    PENDING,
    COMPLETED,
    FAILED
}

data class ClubTransferState(
    val clubId: String,
    val balance: Long,
    val playerIds: List<String>
)

data class CompletedTransfer(
    val id: String,
    val playerId: String,
    val sellingClubId: String?,
    val buyingClubId: String,
    val transferValue: Long,
    val contract: PlayerContract,
    val completedAtSeasonYear: Int,
    val status: TransferCompletionStatus =
        TransferCompletionStatus.COMPLETED
)

data class TransferCompletionResult(
    val transfer: CompletedTransfer?,
    val updatedClubs: List<ClubTransferState>,
    val error: String? = null
) {
    val successful: Boolean
        get() = transfer != null && error == null
}
