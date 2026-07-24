package com.wikisoccerclub.data.career

import com.wikisoccerclub.data.transfer.CareerDate

object CareerDateEngine {
    fun nextDay(date: CareerDate): CareerDate {
        val maximumDay = daysInMonth(date.year, date.month)
        return when {
            date.day < maximumDay -> date.copy(day = date.day + 1)
            date.month < 12 -> CareerDate(date.year, date.month + 1, 1)
            else -> CareerDate(date.year + 1, 1, 1)
        }
    }

    fun advance(date: CareerDate, days: Int): CareerDate {
        require(days >= 0) { "A quantidade de dias não pode ser negativa." }
        var result = date
        repeat(days) { result = nextDay(result) }
        return result
    }

    fun daysInMonth(year: Int, month: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> error("Mês inválido: $month")
    }

    private fun isLeapYear(year: Int): Boolean =
        year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)
}
