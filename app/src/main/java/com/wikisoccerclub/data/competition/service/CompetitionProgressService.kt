package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionProgressEngine
import com.wikisoccerclub.data.competition.engine.GroupTableEngine
import com.wikisoccerclub.data.competition.model.CompetitionMatchResult
import com.wikisoccerclub.data.competition.model.CompetitionProgressSnapshot
import com.wikisoccerclub.data.competition.model.GroupQualificationResult
import com.wikisoccerclub.data.competition.model.KnockoutQualification

class CompetitionProgressService {

    fun calculateGroup(
        groupName: String,
        clubIds: List<String>,
        matches: List<CompetitionMatchResult>
    ): GroupQualificationResult =
        GroupTableEngine.buildTable(
            groupName = groupName,
            clubIds = clubIds,
            matches = matches,
            qualifiedCount = 2
        )

    fun calculateKnockoutPhase(
        phase: String,
        pairings: List<Pair<String, String>>,
        matches: List<CompetitionMatchResult>,
        twoLegged: Boolean
    ): KnockoutQualification =
        CompetitionProgressEngine.resolveKnockoutPhase(
            phase = phase,
            pairings = pairings,
            matches = matches,
            twoLegged = twoLegged
        )

    fun declareChampion(
        snapshot: CompetitionProgressSnapshot,
        championClubId: String,
        runnerUpClubId: String
    ): CompetitionProgressSnapshot =
        CompetitionProgressEngine.finishCompetition(
            snapshot = snapshot,
            championClubId = championClubId,
            runnerUpClubId = runnerUpClubId
        )
}
