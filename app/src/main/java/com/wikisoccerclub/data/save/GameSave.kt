package com.wikisoccerclub.data.save

data class GameSave(
    val managerName: String,
    val clubFileName: String,
    val clubName: String,
    val season: Int = 2026,
    val currentEvent: Int = 0,
    val balance: Long = 0L,
    val transferState: TransferSaveState = TransferSaveState()
)
