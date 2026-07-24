package com.wikisoccerclub.data.youth

class YouthTryoutControlRepository {

    private val controls =
        linkedMapOf<String, YouthTryoutControl>()

    private val signedPlayers =
        linkedMapOf<String, YouthSignedPlayer>()

    fun controlFor(
        clubId: String
    ): YouthTryoutControl =
        controls[clubId]
            ?: YouthTryoutControl(
                clubId = clubId
            )

    fun markTryout(
        clubId: String,
        currentDay: Int
    ) {
        controls[clubId] =
            YouthTryoutControl(
                clubId = clubId,
                lastTryoutDay = currentDay
            )
    }

    fun saveSignedPlayer(
        player: YouthSignedPlayer
    ) {
        signedPlayers[player.id] = player
    }

    fun findSignedPlayer(
        playerId: String
    ): YouthSignedPlayer? =
        signedPlayers[playerId]

    fun allSignedPlayers():
        List<YouthSignedPlayer> =
        signedPlayers.values
            .sortedWith(
                compareBy<YouthSignedPlayer> {
                    it.positionGroup.ordinal
                }.thenByDescending {
                    it.overall
                }
            )
}
