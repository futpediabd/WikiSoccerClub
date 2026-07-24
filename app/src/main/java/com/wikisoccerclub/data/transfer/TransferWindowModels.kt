package com.wikisoccerclub.data.transfer

enum class TransferWindowType(val displayName: String) {
    FIRST("Janela do início da temporada"),
    MID_SEASON("Janela do meio da temporada")
}

data class CareerDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Comparable<CareerDate> {
    init {
        require(month in 1..12) { "Mês inválido." }
        require(day in 1..31) { "Dia inválido." }
    }

    override fun compareTo(other: CareerDate): Int =
        compareValuesBy(this, other, CareerDate::year, CareerDate::month, CareerDate::day)

    fun formatted(): String = "%02d/%02d/%04d".format(day, month, year)
}

data class TransferWindowConfig(
    val type: TransferWindowType,
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int
) {
    init {
        require(startMonth in 1..12 && endMonth in 1..12) { "Mês da janela inválido." }
        require(startDay in 1..31 && endDay in 1..31) { "Dia da janela inválido." }
    }
}

data class TransferSeasonWindowConfig(
    val seasonYear: Int,
    val windows: List<TransferWindowConfig>
)

data class TransferWindow(
    val seasonYear: Int,
    val type: TransferWindowType,
    val startDate: CareerDate,
    val endDate: CareerDate,
    val isOpen: Boolean
) {
    fun contains(date: CareerDate): Boolean = date >= startDate && date <= endDate
}

enum class TransferWindowEventType { OPENED, CLOSED }

data class TransferWindowEvent(
    val id: String,
    val seasonYear: Int,
    val windowType: TransferWindowType,
    val type: TransferWindowEventType,
    val date: CareerDate,
    val title: String,
    val message: String
)

data class TransferWindowStatus(
    val currentDate: CareerDate,
    val windows: List<TransferWindow>,
    val activeWindow: TransferWindow?,
    val nextWindow: TransferWindow?
) {
    val isOpen: Boolean get() = activeWindow != null
}

data class TransferTarget(
    val playerId: String,
    val clubId: String,
    val askingPrice: Long,
    val availableForLoan: Boolean = false
)
