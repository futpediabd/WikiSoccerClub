package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.transfer.CareerDate

class CareerTimelineRepository(
    initialDate: CareerDate = CareerDate(2026, 1, 1)
) {
    private var currentDate = initialDate
    private var dayNumber = 1
    private val events = linkedMapOf<String, CareerGlobalEvent>()

    fun currentDate(): CareerDate = currentDate
    fun currentDayNumber(): Int = dayNumber

    fun updateDate(date: CareerDate) {
        require(date >= currentDate) { "A carreira não pode voltar no tempo." }
        currentDate = date
        dayNumber += 1
    }

    fun saveEvents(items: List<CareerGlobalEvent>) {
        items.forEach { events[it.id] = it }
    }

    fun allEvents(): List<CareerGlobalEvent> =
        events.values.sortedWith(compareByDescending<CareerGlobalEvent> { it.date }.thenBy { it.id })

    fun eventsOn(date: CareerDate): List<CareerGlobalEvent> =
        allEvents().filter { it.date == date }

    fun replace(date: CareerDate, newDayNumber: Int, items: List<CareerGlobalEvent>) {
        require(newDayNumber > 0) { "Número do dia inválido." }
        currentDate = date
        dayNumber = newDayNumber
        events.clear()
        saveEvents(items)
    }

    fun clear(initialDate: CareerDate = CareerDate(2026, 1, 1)) {
        currentDate = initialDate
        dayNumber = 1
        events.clear()
    }
}
