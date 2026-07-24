package com.wikisoccerclub.data.transfer

class ContractRepository {
    private val offers = linkedMapOf<String, ContractOffer>()
    private val contracts = linkedMapOf<String, PlayerContract>()

    fun saveOffer(offer: ContractOffer) {
        offers[offer.id] = offer
    }

    fun saveOffers(values: List<ContractOffer>) {
        values.forEach(::saveOffer)
    }

    fun offers(): List<ContractOffer> = offers.values.toList()

    fun saveContract(contract: PlayerContract) {
        contracts[contract.playerId] = contract
    }

    fun saveContracts(values: List<PlayerContract>) {
        values.forEach(::saveContract)
    }

    fun findContract(playerId: String): PlayerContract? = contracts[playerId]

    fun contracts(): List<PlayerContract> = contracts.values.toList()

    fun expiringContracts(year: Int): List<PlayerContract> =
        contracts.values.filter { it.endYear == year }

    fun removeContract(playerId: String) {
        contracts.remove(playerId)
    }

    fun replaceAll(
        contractOffers: List<ContractOffer>,
        playerContracts: List<PlayerContract>
    ) {
        offers.clear()
        contracts.clear()
        saveOffers(contractOffers)
        saveContracts(playerContracts)
    }

    fun clear() {
        offers.clear()
        contracts.clear()
    }
}
