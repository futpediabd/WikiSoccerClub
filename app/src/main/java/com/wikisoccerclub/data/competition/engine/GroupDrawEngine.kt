package com.wikisoccerclub.data.competition.engine

import com.wikisoccerclub.data.competition.model.DrawClub
import com.wikisoccerclub.data.competition.model.GroupDrawResult
import com.wikisoccerclub.data.competition.model.LiveDrawEvent
import com.wikisoccerclub.data.competition.model.LiveDrawResult
import kotlin.random.Random

class GroupDrawEngine(
    private val random: Random = Random.Default
) {
    fun draw(
        clubs: List<DrawClub>,
        clubsPerGroup: Int = 4,
        maxSameCountryPerGroup: Int = 2,
        maxAttempts: Int = 5_000
    ): LiveDrawResult<GroupDrawResult> {
        require(clubs.isNotEmpty())
        require(clubs.size % clubsPerGroup == 0)

        val numberOfGroups = clubs.size / clubsPerGroup
        val groupNames = (0 until numberOfGroups).map {
            ('A'.code + it).toChar().toString()
        }

        repeat(maxAttempts) { attempt ->
            val groups = groupNames.associateWith { mutableListOf<DrawClub>() }
            val events = mutableListOf<LiveDrawEvent>()
            var valid = true
            var order = 1

            for (club in clubs.shuffled(random)) {
                val availableGroups = groups
                    .filterValues { group ->
                        group.size < clubsPerGroup &&
                            group.count { it.country == club.country } <
                            maxSameCountryPerGroup
                    }
                    .keys
                    .shuffled(random)

                val groupName = availableGroups.firstOrNull()

                if (groupName == null) {
                    valid = false
                    break
                }

                groups.getValue(groupName).add(club)
                events += LiveDrawEvent(
                    order = order++,
                    message = "${club.clubName} foi sorteado para o Grupo $groupName",
                    clubId = club.clubId,
                    groupName = groupName
                )
            }

            if (valid && groups.values.all { it.size == clubsPerGroup }) {
                return LiveDrawResult(
                    result = GroupDrawResult(
                        groups = groups.mapValues { it.value.toList() },
                        attempts = attempt + 1
                    ),
                    events = events
                )
            }
        }

        error("Não foi possível realizar o sorteio respeitando o limite por país.")
    }
}
