package com.wikisoccerclub.data.competition.service

import com.wikisoccerclub.data.competition.engine.GroupDrawEngine
import com.wikisoccerclub.data.competition.engine.KnockoutDrawEngine
import com.wikisoccerclub.data.competition.model.DrawClub
import com.wikisoccerclub.data.competition.model.GroupDrawResult
import com.wikisoccerclub.data.competition.model.KnockoutPairing
import com.wikisoccerclub.data.competition.model.LiveDrawResult

class LiveCompetitionDrawService(
    private val groupDrawEngine: GroupDrawEngine = GroupDrawEngine(),
    private val knockoutDrawEngine: KnockoutDrawEngine = KnockoutDrawEngine()
) {
    fun drawGroups(
        clubs: List<DrawClub>,
        maxSameCountryPerGroup: Int = 2
    ): LiveDrawResult<GroupDrawResult> =
        groupDrawEngine.draw(
            clubs = clubs,
            clubsPerGroup = 4,
            maxSameCountryPerGroup = maxSameCountryPerGroup
        )

    fun drawTwoLegKnockout(
        clubs: List<DrawClub>,
        lowerLevelHostsFirstLeg: Boolean
    ): LiveDrawResult<List<KnockoutPairing>> =
        knockoutDrawEngine.draw(
            clubs = clubs,
            twoLegged = true,
            lowerLevelHostsFirstLeg = lowerLevelHostsFirstLeg
        )

    fun drawSingleMatchKnockout(
        clubs: List<DrawClub>,
        lowerLevelHosts: Boolean
    ): LiveDrawResult<List<KnockoutPairing>> =
        knockoutDrawEngine.draw(
            clubs = clubs,
            twoLegged = false,
            lowerLevelHostsFirstLeg = lowerLevelHosts
        )
}
