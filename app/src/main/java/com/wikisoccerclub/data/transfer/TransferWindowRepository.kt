package com.wikisoccerclub.data.transfer

class TransferWindowRepository {
    private val configs = linkedMapOf<Int, TransferSeasonWindowConfig>()
    private val events = linkedMapOf<String, TransferWindowEvent>()
    private var currentDate = CareerDate(2026, 1, 1)

    fun configure(config: TransferSeasonWindowConfig) {
        configs[config.seasonYear] = config
    }

    fun config(year: Int): TransferSeasonWindowConfig =
        configs[year] ?: TransferWindowEngine.defaultConfig(year).also(::configure)

    fun currentDate(): CareerDate = currentDate

    fun status(date: CareerDate = currentDate): TransferWindowStatus =
        TransferWindowEngine.status(config(date.year), date)

    fun isOpen(date: CareerDate = currentDate): Boolean = status(date).isOpen

    fun requireOpen(date: CareerDate = currentDate) {
        check(isOpen(date)) {
            val next = status(date).nextWindow
            if (next == null) "A janela de transferências está fechada nesta temporada."
            else "A janela de transferências está fechada. Próxima abertura: ${next.startDate.formatted()}."
        }
    }

    fun updateCareerDate(newDate: CareerDate): List<TransferWindowEvent> {
        val generated = TransferWindowEngine.transitionEvents(config(newDate.year), currentDate, newDate)
        generated.forEach { events[it.id] = it }
        currentDate = newDate
        return generated
    }

    fun allEvents(): List<TransferWindowEvent> = events.values.sortedByDescending { it.date }

    fun clear() {
        configs.clear()
        events.clear()
        currentDate = CareerDate(2026, 1, 1)
    }
}
