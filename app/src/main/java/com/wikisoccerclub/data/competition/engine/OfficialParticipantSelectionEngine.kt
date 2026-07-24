package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.OfficialClubCandidate
import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationRequest
import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationResult
import com.wikisoccerclub.data.competition.model.OfficialCompetitionKind

object OfficialParticipantSelectionEngine {

    fun select(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        require(request.desiredParticipants > 0)

        return when (request.kind) {
            OfficialCompetitionKind.STATE_CHAMPIONSHIP ->
                selectStateChampionship(request)

            OfficialCompetitionKind.REGIONAL_CUP ->
                selectRegionalCup(request)

            OfficialCompetitionKind.NATIONAL_CUP ->
                selectNationalCup(request)

            OfficialCompetitionKind.CONTINENTAL_PRIMARY ->
                selectPrimaryContinental(request)

            OfficialCompetitionKind.CONTINENTAL_SECONDARY ->
                selectSecondaryContinental(request)

            OfficialCompetitionKind.SUPER_WORLD_CUP ->
                selectSuperWorldCup(request)
        }
    }

    private fun selectStateChampionship(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val selected = request.candidates
            .filter { request.country == null || it.country == request.country }
            .sortedWith(
                compareBy<OfficialClubCandidate> {
                    it.statePosition ?: Int.MAX_VALUE
                }.thenByDescending { it.level }
            )
            .take(request.desiredParticipants)

        return buildResult(request, selected)
    }

    private fun selectRegionalCup(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val eligible = request.candidates
            .filterNot { it.isInInternationalCompetition }
            .sortedWith(
                compareBy<OfficialClubCandidate> {
                    it.statePosition ?: Int.MAX_VALUE
                }.thenByDescending { it.level }
            )

        val selected = eligible.take(request.desiredParticipants)
        val excluded = request.candidates
            .filter { it.isInInternationalCompetition }
            .map { it.clubId }

        val replacements = selected
            .drop(request.candidates.count {
                !it.isInInternationalCompetition &&
                    (it.statePosition ?: Int.MAX_VALUE) <= 2
            }.coerceAtMost(request.desiredParticipants))
            .map { it.clubId }

        return buildResult(
            request = request,
            selected = selected,
            excluded = excluded,
            replacements = replacements
        )
    }

    private fun selectNationalCup(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val selected = request.candidates
            .sortedWith(
                compareBy<OfficialClubCandidate> {
                    it.leaguePosition ?: Int.MAX_VALUE
                }.thenByDescending { it.level }
            )
            .take(request.desiredParticipants)

        return buildResult(request, selected)
    }

    private fun selectPrimaryContinental(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val priority = request.candidates
            .sortedWith(
                compareByDescending<OfficialClubCandidate> {
                    it.isLeagueChampion
                }.thenByDescending {
                    it.isLeagueRunnerUp
                }.thenByDescending {
                    it.isNationalCupChampion
                }.thenByDescending {
                    it.isContinentalChampion
                }.thenBy {
                    it.leaguePosition ?: Int.MAX_VALUE
                }.thenByDescending {
                    it.level
                }
            )

        val selected = priority
            .filterNot { it.isAlreadyQualified }
            .distinctBy { it.clubId }
            .take(request.desiredParticipants)

        return buildResult(request, selected)
    }

    private fun selectSecondaryContinental(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val priority = request.candidates
            .sortedWith(
                compareByDescending<OfficialClubCandidate> {
                    it.isContinentalRunnerUp
                }.thenBy {
                    it.leaguePosition ?: Int.MAX_VALUE
                }.thenByDescending {
                    it.level
                }
            )

        val selected = priority
            .filterNot { it.isAlreadyQualified }
            .distinctBy { it.clubId }
            .take(request.desiredParticipants)

        return buildResult(request, selected)
    }

    private fun selectSuperWorldCup(
        request: OfficialCompetitionCreationRequest
    ): OfficialCompetitionCreationResult {
        val priority = request.candidates
            .sortedWith(
                compareByDescending<OfficialClubCandidate> {
                    it.isContinentalChampion
                }.thenByDescending {
                    it.isLeagueChampion
                }.thenByDescending {
                    it.isLeagueRunnerUp
                }.thenByDescending {
                    it.isNationalCupChampion
                }.thenBy {
                    it.leaguePosition ?: Int.MAX_VALUE
                }.thenByDescending {
                    it.level
                }
            )

        val selected = priority
            .distinctBy { it.clubId }
            .take(request.desiredParticipants)

        return buildResult(request, selected)
    }

    private fun buildResult(
        request: OfficialCompetitionCreationRequest,
        selected: List<OfficialClubCandidate>,
        excluded: List<String> = emptyList(),
        replacements: List<String> = emptyList()
    ): OfficialCompetitionCreationResult {
        val warnings = mutableListOf<String>()

        if (selected.size < request.desiredParticipants) {
            warnings += "Participantes insuficientes: " +
                "${selected.size}/${request.desiredParticipants}"
        }

        return OfficialCompetitionCreationResult(
            competitionId = request.competitionId,
            season = request.season,
            participantClubIds = selected.map { it.clubId },
            excludedClubIds = excluded.distinct(),
            replacementClubIds = replacements.distinct(),
            warnings = warnings
        )
    }
}
