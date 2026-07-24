package com.wikisoccerclub.core.finance

import com.wikisoccerclub.data.finance.FinanceRepository

/** Repositório financeiro compartilhado entre as telas e o mercado. */
object FinanceModule {
    val repository: FinanceRepository by lazy { FinanceRepository() }
}
