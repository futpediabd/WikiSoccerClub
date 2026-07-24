package com.wikisoccerclub.data.transfer

object TransferWindowEngine {
    fun defaultConfig(year: Int) = TransferSeasonWindowConfig(
        seasonYear = year,
        windows = listOf(
            TransferWindowConfig(TransferWindowType.FIRST, 1, 2, 3, 7),
            TransferWindowConfig(TransferWindowType.MID_SEASON, 7, 10, 9, 2)
        )
    )

    fun windows(
        config: TransferSeasonWindowConfig,
        currentDate: CareerDate
    ): List<TransferWindow> = config.windows.map { item ->
        val start = CareerDate(config.seasonYear, item.startMonth, item.startDay)
        val end = CareerDate(config.seasonYear, item.endMonth, item.endDay)
        TransferWindow(
            seasonYear = config.seasonYear,
            type = item.type,
            startDate = start,
            endDate = end,
            isOpen = currentDate >= start && currentDate <= end
        )
    }

    fun status(
        config: TransferSeasonWindowConfig,
        currentDate: CareerDate
    ): TransferWindowStatus {
        val windows = windows(config, currentDate).sortedBy { it.startDate }
        return TransferWindowStatus(
            currentDate = currentDate,
            windows = windows,
            activeWindow = windows.firstOrNull { it.isOpen },
            nextWindow = windows.firstOrNull { currentDate < it.startDate }
        )
    }

    fun transitionEvents(
        config: TransferSeasonWindowConfig,
        previousDate: CareerDate,
        currentDate: CareerDate
    ): List<TransferWindowEvent> {
        if (currentDate <= previousDate) return emptyList()
        return windows(config, currentDate).flatMap { window ->
            buildList {
                if (window.startDate > previousDate && window.startDate <= currentDate) {
                    add(event(window, TransferWindowEventType.OPENED, window.startDate))
                }
                val closeDate = nextDay(window.endDate)
                if (closeDate > previousDate && closeDate <= currentDate) {
                    add(event(window, TransferWindowEventType.CLOSED, closeDate))
                }
            }
        }.sortedBy { it.date }
    }

    private fun event(
        window: TransferWindow,
        type: TransferWindowEventType,
        date: CareerDate
    ): TransferWindowEvent {
        val opened = type == TransferWindowEventType.OPENED
        return TransferWindowEvent(
            id = "${window.seasonYear}-${window.type.name}-${type.name}",
            seasonYear = window.seasonYear,
            windowType = window.type,
            type = type,
            date = date,
            title = if (opened) "Janela de transferências aberta" else "Janela de transferências fechada",
            message = if (opened) {
                "A ${window.type.displayName.lowercase()} está aberta até ${window.endDate.formatted()}."
            } else {
                "A ${window.type.displayName.lowercase()} foi encerrada. Novas negociações estão bloqueadas."
            }
        )
    }

    private fun nextDay(date: CareerDate): CareerDate {
        val monthDays = if (date.month == 2) 29 else if (date.month in listOf(4, 6, 9, 11)) 30 else 31
        return when {
            date.day < monthDays -> date.copy(day = date.day + 1)
            date.month < 12 -> CareerDate(date.year, date.month + 1, 1)
            else -> CareerDate(date.year + 1, 1, 1)
        }
    }
}
