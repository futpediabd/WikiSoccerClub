package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionHistoryEngine
import com.wikisoccerclub.data.competition.model.ClubCompetitionHistory
import com.wikisoccerclub.data.competition.model.CompetitionMatchResult
import com.wikisoccerclub.data.competition.model.CompetitionSeasonHistory
import com.wikisoccerclub.data.competition.repository.CompetitionHistoryRepository

class CompetitionClosureService(
    private val historyRepository: CompetitionHistoryRepository
) {

    fun closeSeason(
        competitionId: String,
        competitionName: String,
        season: Int,
        participantClubIds: List<String>,
        matches: List<CompetitionMatchResult>,
        championClubId: String,
        runnerUpClubId: String,
        thirdPlaceClubId: String? = null,
        fourthPlaceClubId: String? = null
    ): CompetitionSeasonHistory {
        val seasonHistory = CompetitionSeasonHistory(
            competitionId = competitionId,
            competitionName = competitionName,
            season = season,
            championClubId = championClubId,
            runnerUpClubId = runnerUpClubId,
            thirdPlaceClubId = thirdPlaceClubId,
            fourthPlaceClubId = fourthPlaceClubId,
            participantClubIds = participantClubIds.distinct(),
            totalMatches = matches.size,
            totalGoals = matches.sumOf {
                it.homeGoals + it.awayGoals
            }
        )

        historyRepository.saveSeasonHistory(seasonHistory)

        participantClubIds.distinct().forEach { clubId ->
            val current = historyRepository.getClubHistory(
                clubId = clubId,
                competitionId = competitionId
            ) ?: ClubCompetitionHistory(
                clubId = clubId,
                competitionId = competitionId
            )

            val updated = CompetitionHistoryEngine.updateClubHistory(
                current = current,
                matches = matches,
                championClubId = championClubId,
                runnerUpClubId = runnerUpClubId,
                thirdPlaceClubId = thirdPlaceClubId,
                fourthPlaceClubId = fourthPlaceClubId
            )

            historyRepository.saveClubHistory(updated)
        }

        return seasonHistory
    }
}
