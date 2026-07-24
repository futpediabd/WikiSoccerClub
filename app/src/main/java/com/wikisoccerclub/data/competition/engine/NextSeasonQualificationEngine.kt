package com.wikisoccerclub.data.competition.engine

data class QualificationSource(
    val sourceCompetitionId: String,
    val targetCompetitionId: String,
    val orderedClubIds: List<String>,
    val slots: Int,
    val excludedClubIds: Set<String> = emptySet()
)

object NextSeasonQualificationEngine {

    fun resolve(
        sources: List<QualificationSource>
    ): Map<String, List<String>> {
        val selectedByTarget = linkedMapOf<String, MutableList<String>>()

        sources.forEach { source ->
            val target = selectedByTarget.getOrPut(
                source.targetCompetitionId
            ) { mutableListOf() }

            source.orderedClubIds
                .asSequence()
                .filterNot { it in source.excludedClubIds }
                .filterNot { it in target }
                .take(source.slots)
                .forEach { target += it }
        }

        return selectedByTarget.mapValues { it.value.toList() }
    }
}
