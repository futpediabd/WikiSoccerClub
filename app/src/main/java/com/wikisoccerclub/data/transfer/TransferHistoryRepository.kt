package com.wikisoccerclub.data.transfer

class TransferHistoryRepository {
    private val completed = linkedMapOf<String, CompletedTransfer>()

    fun save(transfer: CompletedTransfer) {
        completed[transfer.id] = transfer
    }

    fun saveAll(values: List<CompletedTransfer>) {
        values.forEach(::save)
    }

    fun find(id: String): CompletedTransfer? = completed[id]

    fun all(): List<CompletedTransfer> =
        completed.values.sortedByDescending { it.completedAtSeasonYear }

    fun byPlayer(playerId: String): List<CompletedTransfer> =
        completed.values.filter { it.playerId == playerId }

    fun byClub(clubId: String): List<CompletedTransfer> =
        completed.values.filter {
            it.sellingClubId == clubId || it.buyingClubId == clubId
        }

    fun bySeason(year: Int): List<CompletedTransfer> =
        completed.values.filter { it.completedAtSeasonYear == year }

    fun replaceAll(values: List<CompletedTransfer>) {
        completed.clear()
        saveAll(values)
    }

    fun clear() {
        completed.clear()
    }
}
