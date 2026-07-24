package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.CompetitionCalendarEngine
import com.wikisoccerclub.data.competition.factory.BuiltOfficialCompetition
import com.wikisoccerclub.data.competition.factory.OfficialCompetitionFactory
import com.wikisoccerclub.data.competition.model.CompetitionCalendarSlot
import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationRequest
import com.wikisoccerclub.data.competition.model.SeasonCompetitionConfig
import com.wikisoccerclub.data.competition.repository.CompetitionRepository

data class BootstrappedOfficialCompetition(
    val built: BuiltOfficialCompetition,
    val calendar: List<CompetitionCalendarSlot>,
    val seasonConfig: SeasonCompetitionConfig
)

class OfficialCompetitionBootstrapService(
    private val competitionRepository: CompetitionRepository
) {

    fun bootstrap(
        request: OfficialCompetitionCreationRequest,
        startDay: Int,
        daysBetweenMatches: Int = 7
    ): BootstrappedOfficialCompetition {
        val built = OfficialCompetitionFactory.build(request)

        val calendar = CompetitionCalendarEngine.createSchedule(
            competitionId = request.competitionId,
            season = request.season,
            startDay = startDay,
            structure = built.structure,
            daysBetweenMatches = daysBetweenMatches
        )

        val config = SeasonCompetitionConfig(
            season = request.season,
            competitionId = request.competitionId,
            participantClubIds =
                built.creation.participantClubIds
        )

        competitionRepository.saveSeasonConfig(config)

        return BootstrappedOfficialCompetition(
            built = built,
            calendar = calendar,
            seasonConfig = config
        )
    }
}
