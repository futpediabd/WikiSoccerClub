package com.wikisoccerclub.data.competition.rules

import com.wikisoccerclub.data.competition.model.*

object OfficialCompetitionRules {
    val stateChampionship = CompetitionRule(
        id = "state_championship", name = "Campeonato Estadual", scope = CompetitionScope.STATE,
        participantOptions = listOf(16), minimumParticipants = 16,
        format = CompetitionFormat.GROUPS_THEN_KNOCKOUT,
        groupRule = GroupRule(groups = 4),
        knockoutRule = KnockoutRule(twoLegged = true, finalTwoLegged = true)
    )

    val regionalCup = stateChampionship.copy(
        id = "regional_cup", name = "Copa Regional", scope = CompetitionScope.REGIONAL,
        notes = listOf("Clubes em competições continentais ou mundiais cedem a vaga ao próximo elegível do estadual.")
    )

    val brazilCup = CompetitionRule(
        id = "brazil_cup", name = "Copa do Brasil", scope = CompetitionScope.NATIONAL,
        participantOptions = listOf(256,128,64,32,16), minimumParticipants = 16,
        format = CompetitionFormat.TWO_LEG_KNOCKOUT,
        knockoutRule = KnockoutRule(twoLegged = true, finalTwoLegged = true, lowerLevelHostsFirstLeg = true)
    )

    val genericNationalCup = brazilCup.copy(
        id = "national_cup", name = "Copa Nacional",
        notes = listOf("Não criar com menos de 16 clubes.", "Usar o maior formato possível.")
    )

    val libertadores = CompetitionRule(
        id = "libertadores", name = "Copa Libertadores", scope = CompetitionScope.CONTINENTAL,
        participantOptions = listOf(32), minimumParticipants = 32,
        format = CompetitionFormat.GROUPS_THEN_KNOCKOUT,
        groupRule = GroupRule(groups = 8),
        knockoutRule = KnockoutRule(twoLegged = true, finalTwoLegged = true)
    )

    val sudamericana = libertadores.copy(id = "sudamericana", name = "Copa Sul-Americana")

    val continental32 = libertadores.copy(id = "continental_32", name = "Competição Continental - 32 clubes")

    val continental64 = CompetitionRule(
        id = "continental_64", name = "Competição Continental - 64 clubes", scope = CompetitionScope.CONTINENTAL,
        participantOptions = listOf(64), minimumParticipants = 64,
        format = CompetitionFormat.TWO_LEG_KNOCKOUT,
        knockoutRule = KnockoutRule(twoLegged = true, finalTwoLegged = true)
    )

    val europaLeague = continental64.copy(id = "europa_league", name = "Liga Europa")

    val superClubWorldCup = CompetitionRule(
        id = "super_club_world_cup", name = "Super Mundial de Clubes", scope = CompetitionScope.WORLD,
        participantOptions = listOf(256,128,64,32), minimumParticipants = 32,
        format = CompetitionFormat.SINGLE_MATCH_KNOCKOUT,
        knockoutRule = KnockoutRule(twoLegged = false, finalTwoLegged = false, lowerLevelHostsFirstLeg = true),
        notes = listOf(
            "Todos os países com liga devem ter ao menos um representante quando houver vagas.",
            "Campeões continentais têm prioridade.",
            "O país do campeão continental recebe vaga adicional.",
            "Se o campeão continental também vencer a liga, a vaga passa ao vice da liga."
        )
    )
}
