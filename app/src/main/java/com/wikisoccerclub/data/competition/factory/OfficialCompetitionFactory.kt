package com.wikisoccerclub.data.competition.factory

import com.wikisoccerclub.data.competition.engine.CompetitionFormatEngine
import com.wikisoccerclub.data.competition.engine.OfficialParticipantSelectionEngine
import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationRequest
import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationResult

data class BuiltOfficialCompetition(
    val creation: OfficialCompetitionCreationResult,
    val structure:
        com.wikisoccerclub.data.competition.engine.GeneratedCompetitionStructure
)

object OfficialCompetitionFactory {

    fun build(
        request: OfficialCompetitionCreationRequest
    ): BuiltOfficialCompetition {
        val creation = OfficialParticipantSelectionEngine.select(request)

        require(creation.participantClubIds.isNotEmpty()) {
            "A competição não possui clubes participantes."
        }

        val structure = CompetitionFormatEngine.generate(
            creation.participantClubIds.size
        )

        return BuiltOfficialCompetition(
            creation = creation,
            structure = structure
        )
    }
}
