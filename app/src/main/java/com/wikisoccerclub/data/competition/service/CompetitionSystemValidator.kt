package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.model.OfficialCompetitionCreationRequest

data class CompetitionValidationReport(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

object CompetitionSystemValidator {

    fun validate(
        request: OfficialCompetitionCreationRequest
    ): CompetitionValidationReport {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (request.competitionId.isBlank()) {
            errors += "O identificador da competição está vazio."
        }

        if (request.competitionName.isBlank()) {
            errors += "O nome da competição está vazio."
        }

        if (request.desiredParticipants <= 0) {
            errors += "A quantidade de participantes é inválida."
        }

        if (request.candidates.isEmpty()) {
            errors += "Nenhum clube candidato foi informado."
        }

        val duplicateIds = request.candidates
            .groupingBy { it.clubId }
            .eachCount()
            .filterValues { it > 1 }
            .keys

        if (duplicateIds.isNotEmpty()) {
            errors += "Clubes duplicados: ${duplicateIds.joinToString()}"
        }

        if (request.candidates.size < request.desiredParticipants) {
            warnings += "Há menos candidatos do que vagas."
        }

        return CompetitionValidationReport(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }
}
