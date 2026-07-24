package com.wikisoccerclub.data.match

import com.wikisoccerclub.data.competition.MatchSubstitution

data class LiveMatchSession(
    val match: LiveMatchState = LiveMatchState(),
    val homeTeam: LiveTeamState,
    val awayTeam: LiveTeamState,
    val substitutions: List<MatchSubstitution> = emptyList()
)
