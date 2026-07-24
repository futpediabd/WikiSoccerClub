package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.transfer.CareerDate

/** Agenda compartilhada da carreira. Pode ser serializada pelo módulo de salvamento. */
class CareerScheduleRepository(
    initialMatches: List<ScheduledCareerMatch> = emptyList()
) {
    private val matches = linkedMapOf<String, ScheduledCareerMatch>()

    init { saveAll(initialMatches) }

    fun save(match: ScheduledCareerMatch) {
        matches[match.id] = match
    }

    fun saveAll(items: List<ScheduledCareerMatch>) = items.forEach(::save)

    fun all(): List<ScheduledCareerMatch> = matches.values.sortedWith(
        compareBy<ScheduledCareerMatch> { it.date }.thenBy { it.competitionName }.thenBy { it.id }
    )

    fun on(date: CareerDate): List<ScheduledCareerMatch> = all().filter { it.date == date }

    fun pendingOn(date: CareerDate): List<ScheduledCareerMatch> = on(date).filter {
        it.status == ScheduledMatchStatus.SCHEDULED || it.status == ScheduledMatchStatus.READY_FOR_USER
    }

    fun nextForClub(clubId: String, after: CareerDate): ScheduledCareerMatch? = all().firstOrNull {
        it.date > after &&
            (it.homeClubId == clubId || it.awayClubId == clubId) &&
            it.status != ScheduledMatchStatus.COMPLETED &&
            it.status != ScheduledMatchStatus.SIMULATED
    }

    fun nextMatchDate(after: CareerDate): CareerDate? = all().firstOrNull {
        it.date > after &&
            it.status != ScheduledMatchStatus.COMPLETED &&
            it.status != ScheduledMatchStatus.SIMULATED
    }?.date

    fun updateStatus(id: String, status: ScheduledMatchStatus): ScheduledCareerMatch? {
        val current = matches[id] ?: return null
        return current.copy(status = status).also { matches[id] = it }
    }

    fun complete(id: String, homeGoals: Int, awayGoals: Int): ScheduledCareerMatch? {
        require(homeGoals >= 0 && awayGoals >= 0) { "O placar não pode ser negativo." }
        val current = matches[id] ?: return null
        return current.copy(
            status = if (current.involvesUserClub) ScheduledMatchStatus.COMPLETED else ScheduledMatchStatus.SIMULATED,
            homeGoals = homeGoals,
            awayGoals = awayGoals
        ).also { matches[id] = it }
    }
}
