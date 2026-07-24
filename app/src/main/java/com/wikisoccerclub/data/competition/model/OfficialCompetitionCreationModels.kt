package com.wikisoccerclub.data.competition.model

enum class OfficialCompetitionKind {
    STATE_CHAMPIONSHIP,
    REGIONAL_CUP,
    NATIONAL_CUP,
    CONTINENTAL_PRIMARY,
    CONTINENTAL_SECONDARY,
    SUPER_WORLD_CUP
}

data class OfficialClubCandidate(
    val clubId: String,
    val clubName: String,
    val country: String,
    val state: String? = null,
    val leaguePosition: Int? = null,
    val statePosition: Int? = null,
    val level: Int,
    val isLeagueChampion: Boolean = false,
    val isLeagueRunnerUp: Boolean = false,
    val isNationalCupChampion: Boolean = false,
    val isContinentalChampion: Boolean = false,
    val isContinentalRunnerUp: Boolean = false,
    val isAlreadyQualified: Boolean = false,
    val isInInternationalCompetition: Boolean = false
)

data class OfficialCompetitionCreationRequest(
    val competitionId: String,
    val competitionName: String,
    val kind: OfficialCompetitionKind,
    val season: Int,
    val country: String? = null,
    val region: String? = null,
    val desiredParticipants: Int,
    val candidates: List<OfficialClubCandidate>
)

data class OfficialCompetitionCreationResult(
    val competitionId: String,
    val season: Int,
    val participantClubIds: List<String>,
    val excludedClubIds: List<String>,
    val replacementClubIds: List<String>,
    val warnings: List<String>
)
