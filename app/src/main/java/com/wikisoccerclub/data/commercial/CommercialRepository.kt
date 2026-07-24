package com.wikisoccerclub.data.commercial

class CommercialRepository {

    private val offers =
        linkedMapOf<String, SponsorOffer>()

    private val contracts =
        linkedMapOf<String, SponsorContract>()

    private val merchandiseResults =
        linkedMapOf<Int, MerchandiseResult>()

    fun saveOffer(offer: SponsorOffer) {
        offers[offer.id] = offer
    }

    fun saveOffers(values: List<SponsorOffer>) {
        values.forEach(::saveOffer)
    }

    fun availableOffers():
        List<SponsorOffer> =
        offers.values.toList()

    fun removeOffer(offerId: String) {
        offers.remove(offerId)
    }

    fun saveContract(contract: SponsorContract) {
        contracts[contract.id] = contract
    }

    fun findContract(
        contractId: String
    ): SponsorContract? =
        contracts[contractId]

    fun activeContracts():
        List<SponsorContract> =
        contracts.values.filter {
            it.status == ContractStatus.ACTIVE
        }

    fun allContracts():
        List<SponsorContract> =
        contracts.values.toList()

    fun saveMerchandiseResult(
        result: MerchandiseResult
    ) {
        merchandiseResults[result.seasonYear] =
            result
    }

    fun merchandiseResult(
        seasonYear: Int
    ): MerchandiseResult? =
        merchandiseResults[seasonYear]

    fun merchandiseHistory():
        List<MerchandiseResult> =
        merchandiseResults.values
            .sortedByDescending { it.seasonYear }
}
