package com.wikisoccerclub.data.competition.model

data class DrawClub(
    val clubId: String,
    val clubName: String,
    val country: String,
    val level: Int
)

data class GroupDrawResult(
    val groups: Map<String, List<DrawClub>>,
    val attempts: Int
)

data class KnockoutPairing(
    val pairingNumber: Int,
    val firstLegHome: DrawClub,
    val firstLegAway: DrawClub,
    val secondLegHome: DrawClub? = null,
    val secondLegAway: DrawClub? = null
)

data class LiveDrawEvent(
    val order: Int,
    val message: String,
    val clubId: String? = null,
    val groupName: String? = null,
    val pairingNumber: Int? = null
)

data class LiveDrawResult<T>(
    val result: T,
    val events: List<LiveDrawEvent>
)
