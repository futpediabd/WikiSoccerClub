package com.wikisoccerclub.data.competition.model

enum class CompetitionScope { STATE, REGIONAL, NATIONAL, CONTINENTAL, WORLD }
enum class CompetitionFormat { GROUPS_THEN_KNOCKOUT, TWO_LEG_KNOCKOUT, SINGLE_MATCH_KNOCKOUT }

data class ClubEntry(
    val clubId: String,
    val clubName: String,
    val country: String,
    val state: String? = null,
    val level: Int,
    val leaguePosition: Int? = null,
    val isLeagueChampion: Boolean = false,
    val isLeagueRunnerUp: Boolean = false,
    val isNationalCupChampion: Boolean = false,
    val isContinentalChampion: Boolean = false,
    val isContinentalRunnerUp: Boolean = false
)

data class GroupRule(
    val groups: Int,
    val clubsPerGroup: Int = 4,
    val roundRobinLegs: Int = 2,
    val qualifiedPerGroup: Int = 2,
    val maxClubsSameCountry: Int = 2
)

data class KnockoutRule(
    val twoLegged: Boolean,
    val finalTwoLegged: Boolean,
    val awayGoals: Boolean = false,
    val extraTime: Boolean = false,
    val penaltiesOnTie: Boolean = true,
    val liveDrawEveryRound: Boolean = true,
    val lowerLevelHostsFirstLeg: Boolean = false
)

data class CompetitionRule(
    val id: String,
    val name: String,
    val scope: CompetitionScope,
    val participantOptions: List<Int>,
    val minimumParticipants: Int,
    val format: CompetitionFormat,
    val groupRule: GroupRule? = null,
    val knockoutRule: KnockoutRule,
    val notes: List<String> = emptyList()
)

data class QualificationResult(
    val selectedClubs: List<ClubEntry>,
    val participantCount: Int,
    val warnings: List<String> = emptyList()
)

data class DrawPairing(val homeClub: ClubEntry, val awayClub: ClubEntry)
