package com.wikisoccerclub.ui.match

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.match.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LiveMatchViewModel(
    initialSession: LiveMatchSession,
    private val engine: MinuteMatchEngine = MinuteMatchEngine()
) : ViewModel() {

    private val _session = MutableStateFlow(initialSession)
    val session: StateFlow<LiveMatchSession> = _session.asStateFlow()

    fun advance() {
        repeat(_session.value.match.speed.multiplier) {
            val current = _session.value

            if (current.match.finished ||
                current.match.paused ||
                current.match.pausedForSubstitution
            ) return@repeat

            val advancedMatch = engine.advance(
                state = current.match,
                homeTeamId = current.homeTeam.teamId,
                awayTeamId = current.awayTeam.teamId
            )

            val updatedHome = LiveEnergyEngine.updateTeam(
                team = current.homeTeam,
                minute = advancedMatch.currentMinute
            )

            val updatedAway = LiveEnergyEngine.updateTeam(
                team = current.awayTeam,
                minute = advancedMatch.currentMinute
            )

            _session.value = current.copy(
                match = advancedMatch,
                homeTeam = updatedHome,
                awayTeam = updatedAway
            )
        }
    }

    fun togglePause() {
        val current = _session.value
        if (current.match.finished ||
            current.match.pausedForSubstitution
        ) return

        _session.value = current.copy(
            match = current.match.copy(
                paused = !current.match.paused
            )
        )
    }

    fun changeSpeed(speed: MatchSpeed) {
        val current = _session.value
        _session.value = current.copy(
            match = current.match.copy(speed = speed)
        )
    }

    fun substituteHome(
        playerOutId: String,
        playerInId: String
    ): String {
        val current = _session.value
        val result = LiveSubstitutionEngine.execute(
            team = current.homeTeam,
            minute = current.match.currentMinute,
            playerOutId = playerOutId,
            playerInId = playerInId
        )

        _session.value = current.copy(
            homeTeam = result.team,
            substitutions = result.substitution?.let {
                current.substitutions + it
            } ?: current.substitutions,
            match = result.substitution?.let {
                current.match.copy(
                    events = current.match.events + LiveMatchEvent(
                        id = "sub_home_${current.match.currentMinute}_${current.substitutions.size}",
                        minute = current.match.currentMinute,
                        type = LiveMatchEventType.SUBSTITUTION,
                        teamId = current.homeTeam.teamId,
                        description = result.message
                    )
                )
            } ?: current.match
        )

        return result.message
    }

    fun substituteAway(
        playerOutId: String,
        playerInId: String
    ): String {
        val current = _session.value
        val result = LiveSubstitutionEngine.execute(
            team = current.awayTeam,
            minute = current.match.currentMinute,
            playerOutId = playerOutId,
            playerInId = playerInId
        )

        _session.value = current.copy(
            awayTeam = result.team,
            substitutions = result.substitution?.let {
                current.substitutions + it
            } ?: current.substitutions,
            match = result.substitution?.let {
                current.match.copy(
                    events = current.match.events + LiveMatchEvent(
                        id = "sub_away_${current.match.currentMinute}_${current.substitutions.size}",
                        minute = current.match.currentMinute,
                        type = LiveMatchEventType.SUBSTITUTION,
                        teamId = current.awayTeam.teamId,
                        description = result.message
                    )
                )
            } ?: current.match
        )

        return result.message
    }

    fun resumeAfterHalfTime() {
        val current = _session.value
        _session.value = current.copy(
            match = engine.resumeAfterHalfTime(current.match)
        )
    }
}
