package com.wikisoccerclub.data.competition.engine

data class LeagueFinalPosition(
    val clubId: String,
    val position: Int
)

data class QualificationSlot(
    val targetCompetitionId: String,
    val positions: IntRange
)

data class FinalizedQualification(
    val targetCompetitionId: String,
    val qualifiedClubIds: List<String>
)

object QualificationFinalizerEngine {

    fun finalize(
        finalTable: List<LeagueFinalPosition>,
        slots: List<QualificationSlot>,
        ineligibleClubIds: Set<String> = emptySet()
    ): List<FinalizedQualification> {
        val ordered = finalTable.sortedBy { it.position }

        return slots.map { slot ->
            val required = slot.positions.count()

            val selected = ordered
                .filter { it.clubId !in ineligibleClubIds }
                .filter { it.position >= slot.positions.first }
                .take(required)
                .map { it.clubId }

            FinalizedQualification(
                targetCompetitionId = slot.targetCompetitionId,
                qualifiedClubIds = selected
            )
        }
    }
}
