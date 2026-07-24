package com.wikisoccerclub.core.career

import com.wikisoccerclub.data.career.*
import com.wikisoccerclub.data.transfer.CareerDate
import com.wikisoccerclub.data.transfer.TransferWindowEvent
import com.wikisoccerclub.data.transfer.TransferWindowEventType
import com.wikisoccerclub.data.transfer.TransferWindowRepository

/**
 * Avança a data da carreira e transforma mudanças da janela de transferências
 * em eventos globais consumíveis pelas telas, notificações e pelo salvamento.
 */
class CareerProgressionService(
    private val timeline: CareerTimelineRepository,
    private val windows: TransferWindowRepository
) {
    fun currentState(): CareerDayState = stateFor(
        timeline.currentDate(),
        timeline.currentDayNumber(),
        timeline.eventsOn(timeline.currentDate())
    )

    fun advanceOneDay(extraEvents: List<CareerGlobalEvent> = emptyList()): CareerAdvanceResult {
        val previous = timeline.currentDate()
        val next = CareerDateEngine.nextDay(previous)
        val generated = buildList {
            addAll(windows.updateCareerDate(next).map(::toGlobalEvent))
            if (previous.year != next.year) {
                add(CareerGlobalEvent(
                    id = "season-start-${next.year}",
                    date = next,
                    type = CareerGlobalEventType.SEASON_STARTED,
                    title = "Nova temporada",
                    message = "A temporada ${next.year} começou.",
                    important = true
                ))
            }
            addAll(extraEvents.filter { it.date == next })
        }

        timeline.updateDate(next)
        timeline.saveEvents(generated)
        val state = stateFor(next, timeline.currentDayNumber(), generated)
        return CareerAdvanceResult(previous, state, generated)
    }

    fun advanceDays(days: Int): List<CareerAdvanceResult> {
        require(days >= 0) { "A quantidade de dias não pode ser negativa." }
        return buildList { repeat(days) { add(advanceOneDay()) } }
    }

    fun registerMatchDay(
        date: CareerDate,
        competition: String,
        opponent: String,
        home: Boolean
    ): CareerGlobalEvent = CareerGlobalEvent(
        id = "match-${date.year}-${date.month}-${date.day}-${competition.hashCode()}-${opponent.hashCode()}",
        date = date,
        type = CareerGlobalEventType.MATCH_DAY,
        title = "Dia de jogo",
        message = "$competition: ${if (home) "em casa" else "fora"} contra $opponent.",
        important = true
    ).also { timeline.saveEvents(listOf(it)) }

    private fun stateFor(
        date: CareerDate,
        dayNumber: Int,
        events: List<CareerGlobalEvent>
    ) = CareerDayState(
        currentDate = date,
        seasonYear = date.year,
        dayNumber = dayNumber,
        transferWindowOpen = windows.isOpen(date),
        generatedEvents = events
    )

    private fun toGlobalEvent(event: TransferWindowEvent): CareerGlobalEvent = CareerGlobalEvent(
        id = "career-${event.id}",
        date = event.date,
        type = when (event.type) {
            TransferWindowEventType.OPENED -> CareerGlobalEventType.TRANSFER_WINDOW_OPENED
            TransferWindowEventType.CLOSED -> CareerGlobalEventType.TRANSFER_WINDOW_CLOSED
        },
        title = event.title,
        message = event.message,
        important = true
    )
}
