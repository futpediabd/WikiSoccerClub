package com.wikisoccerclub.data.match

import com.wikisoccerclub.data.competition.CompetitionPlayer

data class LivePlayerState(
    val player: CompetitionPlayer,
    val energy: Int = 100,
    val onField: Boolean = false,
    val substitutedOut: Boolean = false
)
