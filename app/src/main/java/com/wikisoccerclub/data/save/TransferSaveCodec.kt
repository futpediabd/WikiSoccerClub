package com.wikisoccerclub.data.save

import com.wikisoccerclub.data.transfer.*
import java.net.URLDecoder
import java.net.URLEncoder

object TransferSaveCodec {
    private const val SECTION = "\n"
    private const val FIELD = "\t"
    private const val LIST = ","

    fun encode(state: TransferSaveState): String = buildList {
        state.offers.forEach { add(row("O", it.id, it.playerId, it.sellingClubId, it.buyingClubId, it.value, it.installmentValue, it.installments, it.status.name)) }
        state.contractOffers.forEach { add(row("CO", it.id, it.playerId, it.clubId, it.startYear, it.endYear, it.monthlySalary, it.signingBonus, it.releaseClause ?: "", it.status.name)) }
        state.contracts.forEach { add(row("C", it.playerId, it.clubId, it.startYear, it.endYear, it.monthlySalary, it.releaseClause ?: "")) }
        state.loanOffers.forEach { add(row("LO", it.id, it.playerId, it.ownerClubId, it.destinationClubId, it.startYear, it.endYear, it.monthlyFee, it.wagePercentagePaidByDestination, it.purchaseOption ?: "", it.status.name)) }
        state.activeLoans.forEach { add(row("L", it.offerId, it.playerId, it.ownerClubId, it.destinationClubId, it.endYear, it.purchaseOption ?: "")) }
        state.clubs.forEach { add(row("CL", it.clubId, it.balance, it.playerIds.joinToString(LIST) { id -> esc(id) })) }
        state.history.forEach {
            add(row(
                "H", it.id, it.playerId, it.sellingClubId ?: "", it.buyingClubId,
                it.transferValue, it.contract.playerId, it.contract.clubId,
                it.contract.startYear, it.contract.endYear, it.contract.monthlySalary,
                it.contract.releaseClause ?: "", it.completedAtSeasonYear, it.status.name
            ))
        }
    }.joinToString(SECTION)

    fun decode(raw: String): TransferSaveState {
        if (raw.isBlank()) return TransferSaveState()
        val offers = mutableListOf<TransferOffer>()
        val contractOffers = mutableListOf<ContractOffer>()
        val contracts = mutableListOf<PlayerContract>()
        val loanOffers = mutableListOf<LoanOffer>()
        val loans = mutableListOf<ActiveLoan>()
        val clubs = mutableListOf<ClubTransferState>()
        val history = mutableListOf<CompletedTransfer>()

        raw.lineSequence().filter { it.isNotBlank() }.forEach { line ->
            runCatching {
                val p = line.split(FIELD).map(::unesc)
                when (p.firstOrNull()) {
                    "O" -> offers += TransferOffer(p[1], p[2], p[3], p[4], p[5].toLong(), p[6].toLong(), p[7].toInt(), OfferStatus.valueOf(p[8]))
                    "CO" -> contractOffers += ContractOffer(p[1], p[2], p[3], p[4].toInt(), p[5].toInt(), p[6].toLong(), p[7].toLong(), p[8].longOrNull(), ContractNegotiationStatus.valueOf(p[9]))
                    "C" -> contracts += PlayerContract(p[1], p[2], p[3].toInt(), p[4].toInt(), p[5].toLong(), p[6].longOrNull())
                    "LO" -> loanOffers += LoanOffer(p[1], p[2], p[3], p[4], p[5].toInt(), p[6].toInt(), p[7].toLong(), p[8].toInt(), p[9].longOrNull(), LoanStatus.valueOf(p[10]))
                    "L" -> loans += ActiveLoan(p[1], p[2], p[3], p[4], p[5].toInt(), p[6].longOrNull())
                    "CL" -> clubs += ClubTransferState(p[1], p[2].toLong(), p.getOrElse(3) { "" }.split(LIST).filter(String::isNotBlank))
                    "H" -> {
                        val contract = PlayerContract(p[6], p[7], p[8].toInt(), p[9].toInt(), p[10].toLong(), p[11].longOrNull())
                        history += CompletedTransfer(p[1], p[2], p[3].ifBlank { null }, p[4], p[5].toLong(), contract, p[12].toInt(), TransferCompletionStatus.valueOf(p[13]))
                    }
                }
            }
        }
        return TransferSaveState(offers, contractOffers, contracts, loanOffers, loans, clubs, history)
    }

    private fun row(vararg values: Any): String =
        values.joinToString(FIELD) { esc(it.toString()) }

    private fun esc(value: String): String =
        URLEncoder.encode(value, Charsets.UTF_8.name())

    private fun unesc(value: String): String =
        URLDecoder.decode(value, Charsets.UTF_8.name())

    private fun String.longOrNull(): Long? = ifBlank { null }?.toLongOrNull()
}
