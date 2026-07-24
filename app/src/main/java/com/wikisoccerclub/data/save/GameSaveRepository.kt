package com.wikisoccerclub.data.save

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.gameDataStore by preferencesDataStore(name = "wsc_game_save")

class GameSaveRepository(private val context: Context) {
    private object Keys {
        val managerName = stringPreferencesKey("manager_name")
        val clubFileName = stringPreferencesKey("club_file_name")
        val clubName = stringPreferencesKey("club_name")
        val season = intPreferencesKey("season")
        val currentEvent = intPreferencesKey("current_event")
        val balance = longPreferencesKey("balance")
        val transferState = stringPreferencesKey("transfer_state_v1")
    }

    val saveFlow: Flow<GameSave?> = context.gameDataStore.data.map { preferences ->
        val clubName = preferences[Keys.clubName] ?: return@map null
        GameSave(
            managerName = preferences[Keys.managerName].orEmpty(),
            clubFileName = preferences[Keys.clubFileName].orEmpty(),
            clubName = clubName,
            season = preferences[Keys.season] ?: 2026,
            currentEvent = preferences[Keys.currentEvent] ?: 0,
            balance = preferences[Keys.balance] ?: 0L,
            transferState = TransferSaveCodec.decode(
                preferences[Keys.transferState].orEmpty()
            )
        )
    }

    suspend fun save(gameSave: GameSave) {
        context.gameDataStore.edit { preferences ->
            preferences[Keys.managerName] = gameSave.managerName
            preferences[Keys.clubFileName] = gameSave.clubFileName
            preferences[Keys.clubName] = gameSave.clubName
            preferences[Keys.season] = gameSave.season
            preferences[Keys.currentEvent] = gameSave.currentEvent
            preferences[Keys.balance] = gameSave.balance
            preferences[Keys.transferState] =
                TransferSaveCodec.encode(gameSave.transferState)
        }
    }

    suspend fun updateCurrentEvent(currentEvent: Int) {
        context.gameDataStore.edit { preferences ->
            preferences[Keys.currentEvent] = currentEvent
        }
    }

    suspend fun updateTransferState(state: TransferSaveState) {
        context.gameDataStore.edit { preferences ->
            preferences[Keys.transferState] = TransferSaveCodec.encode(state)
        }
    }

    suspend fun clear() {
        context.gameDataStore.edit { it.clear() }
    }
}
