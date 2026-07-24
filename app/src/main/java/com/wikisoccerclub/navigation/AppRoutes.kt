package com.wikisoccerclub.navigation

object AppRoutes {
    const val MENU = "menu"
    const val NEW_GAME = "new_game"
    const val LOAD_GAME = "load_game"
    const val EDITOR = "editor"
    const val CREDITS = "credits"
    const val HOME = "home"
    const val SQUAD = "squad"
    const val LINEUP = "lineup"
    const val MATCH = "match"
    const val COMPETITION = "competition"
    const val STANDINGS = "standings"
    const val FIXTURES = "fixtures"
    const val CURRENT_ROUND = "current_round"
    const val TOP_SCORERS = "top_scorers"
    const val DISCIPLINE = "discipline"
    const val INJURIES = "injuries"
    const val AVAILABLE_SQUAD = "available_squad"
    const val COMPETITION_LINEUP = "competition_lineup"
    const val COMPETITION_MATCH_DETAILS = "competition_match_details/{matchId}"
    const val TRANSFER_MARKET = "transfer_market"
    const val TRANSFER_WINDOW = "transfer_window"
    const val TRANSFER_OFFERS = "transfer_offers"
    const val TRANSFER_CONTRACTS = "transfer_contracts"
    const val TRANSFER_LOANS = "transfer_loans"
    const val TRANSFER_AI = "transfer_ai"
    const val TRANSFER_HISTORY = "transfer_history"

    fun competitionMatchDetails(matchId: String): String =
        "competition_match_details/$matchId"
}
