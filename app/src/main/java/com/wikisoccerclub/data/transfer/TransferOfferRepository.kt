package com.wikisoccerclub.data.transfer

class TransferOfferRepository {
    private val offers = linkedMapOf<String, TransferOffer>()

    fun save(offer: TransferOffer) {
        offers[offer.id] = offer
    }

    fun saveAll(values: List<TransferOffer>) {
        values.forEach(::save)
    }

    fun all(): List<TransferOffer> = offers.values.toList()

    fun pending(): List<TransferOffer> =
        offers.values.filter { it.status == OfferStatus.PENDING }

    fun replaceAll(values: List<TransferOffer>) {
        offers.clear()
        saveAll(values)
    }

    fun clear() {
        offers.clear()
    }
}
