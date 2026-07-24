package com.wikisoccerclub.data.competition.engine

enum class GeneratedCompetitionFormat {
    GROUPS_OF_FOUR_THEN_KNOCKOUT,
    KNOCKOUT_FROM_START
}

data class GeneratedCompetitionStructure(
    val participants: Int,
    val format: GeneratedCompetitionFormat,
    val groups: Int,
    val clubsPerGroup: Int,
    val knockoutRounds: List<String>
)

object CompetitionFormatEngine {

    fun generate(participants: Int): GeneratedCompetitionStructure {
        require(participants > 0)

        return when {
            participants <= 32 -> {
                require(participants % 4 == 0) {
                    "Competições com até 32 clubes precisam ter quantidade divisível por 4."
                }

                GeneratedCompetitionStructure(
                    participants = participants,
                    format = GeneratedCompetitionFormat.GROUPS_OF_FOUR_THEN_KNOCKOUT,
                    groups = participants / 4,
                    clubsPerGroup = 4,
                    knockoutRounds = knockoutRounds(participants / 2)
                )
            }

            participants == 64 -> GeneratedCompetitionStructure(
                participants = 64,
                format = GeneratedCompetitionFormat.KNOCKOUT_FROM_START,
                groups = 0,
                clubsPerGroup = 0,
                knockoutRounds = listOf(
                    "Primeira fase",
                    "Segunda fase",
                    "Oitavas de final",
                    "Quartas de final",
                    "Semifinais",
                    "Final"
                )
            )

            else -> GeneratedCompetitionStructure(
                participants = participants,
                format = GeneratedCompetitionFormat.KNOCKOUT_FROM_START,
                groups = 0,
                clubsPerGroup = 0,
                knockoutRounds = knockoutRounds(participants)
            )
        }
    }

    private fun knockoutRounds(initialClubs: Int): List<String> {
        var clubs = initialClubs
        val rounds = mutableListOf<String>()

        while (clubs > 1) {
            rounds += when (clubs) {
                2 -> "Final"
                4 -> "Semifinais"
                8 -> "Quartas de final"
                16 -> "Oitavas de final"
                32 -> "Segunda fase"
                64 -> "Primeira fase"
                else -> "Fase de $clubs clubes"
            }
            clubs /= 2
        }

        return rounds
    }
}
