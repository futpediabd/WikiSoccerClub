package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.CompetitionIntegrationRequest

data class IntegrationValidationResult(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

object CompetitionIntegrationValidationEngine {

    fun validate(
        request: CompetitionIntegrationRequest
    ): IntegrationValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (request.season <= 0) {
            errors += "A temporada deve ser maior que zero."
        }

        if (request.officialCompetitions.isEmpty()) {
            errors += "Nenhuma competição foi informada."
        }

        if (request.daysBetweenMatches <= 0) {
            errors += "O intervalo entre partidas deve ser maior que zero."
        }

        val repeatedIds = request.officialCompetitions
            .groupingBy { it.competitionId }
            .eachCount()
            .filterValues { it > 1 }
            .keys

        if (repeatedIds.isNotEmpty()) {
            errors += "Competições duplicadas: ${repeatedIds.joinToString()}"
        }

        request.officialCompetitions.forEach { competition ->
            if (competition.season != request.season) {
                errors += "A competição ${competition.competitionId} " +
                    "está cadastrada em outra temporada."
            }

            if (competition.competitionId !in request.competitionStartDays) {
                warnings += "Data inicial ausente para " +
                    competition.competitionId
            }

            if (competition.candidates.isEmpty()) {
                errors += "A competição ${competition.competitionId} " +
                    "não possui candidatos."
            }
        }

        return IntegrationValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }
}
