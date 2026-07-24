package com.wikisoccerclub.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wikisoccerclub.data.ban.BanAssetRepository
import com.wikisoccerclub.data.ban.BanClub
import com.wikisoccerclub.data.calendar.MatchEvent
import com.wikisoccerclub.data.calendar.SeasonCalendar
import com.wikisoccerclub.data.save.GameSave
import com.wikisoccerclub.data.save.GameSaveRepository
import com.wikisoccerclub.data.simulation.MatchResult
import com.wikisoccerclub.data.simulation.MatchSimulator
import com.wikisoccerclub.data.squad.MatchLineup
import com.wikisoccerclub.data.squad.SquadFactory
import com.wikisoccerclub.data.squad.SquadPlayer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val banRepository = BanAssetRepository(application)
    private val saveRepository = GameSaveRepository(application)

    val clubs: List<BanClub> = banRepository.loadAllClubs()

    val savedGame = saveRepository.saveFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    var lastMatchResult: MatchResult? = null
        private set

    fun currentClub(): BanClub? =
        clubs.firstOrNull { it.sourceFile == savedGame.value?.clubFileName }

    fun currentSquad(): List<SquadPlayer> =
        SquadFactory.fromClub(currentClub())

    fun automaticLineup(): MatchLineup =
        SquadFactory.automaticLineup(currentSquad())

    fun nextMatch(): MatchEvent? {
        val save = savedGame.value ?: return null
        return SeasonCalendar.nextMatch(save.clubName, save.currentEvent)
    }

    fun createGame(managerName: String, club: BanClub, onSaved: () -> Unit) {
        viewModelScope.launch {
            saveRepository.save(
                GameSave(
                    managerName = managerName.trim().ifBlank { "Treinador" },
                    clubFileName = club.sourceFile,
                    clubName = club.name,
                    season = 2026,
                    currentEvent = 0,
                    balance = 0L
                )
            )
            onSaved()
        }
    }

    fun playNextMatch(lineup: MatchLineup, onFinished: (MatchResult) -> Unit) {
        val save = savedGame.value ?: return
        val match = nextMatch() ?: return

        val result = MatchSimulator.simulate(
            lineup = lineup,
            userIsHome = match.home == save.clubName
        )

        lastMatchResult = result

        viewModelScope.launch {
            saveRepository.updateCurrentEvent(match.id)
            onFinished(result)
        }
    }
}
