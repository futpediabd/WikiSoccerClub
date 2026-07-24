package com.wikisoccerclub.data.transfer

class TransferAiRepository {

    private val clubProfiles =
        linkedMapOf<String, AiClubProfile>()

    private val marketPlayers =
        linkedMapOf<String, MarketPlayer>()

    private val decisions =
        mutableListOf<AiTransferDecision>()

    fun saveClub(profile: AiClubProfile) {
        clubProfiles[profile.clubId] = profile
    }

    fun saveClubs(profiles: List<AiClubProfile>) {
        profiles.forEach(::saveClub)
    }

    fun clubs(): List<AiClubProfile> =
        clubProfiles.values.toList()

    fun savePlayer(player: MarketPlayer) {
        marketPlayers[player.playerId] = player
    }

    fun savePlayers(players: List<MarketPlayer>) {
        players.forEach(::savePlayer)
    }

    fun players(): List<MarketPlayer> =
        marketPlayers.values.toList()

    fun saveDecision(decision: AiTransferDecision) {
        decisions.removeAll {
            it.clubId == decision.clubId &&
                it.playerId == decision.playerId
        }
        decisions += decision
    }

    fun decisions(): List<AiTransferDecision> =
        decisions.sortedByDescending { it.score }

    fun clearDecisions() {
        decisions.clear()
    }
}
