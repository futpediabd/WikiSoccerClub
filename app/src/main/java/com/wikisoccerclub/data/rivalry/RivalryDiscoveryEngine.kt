package com.wikisoccerclub.data.rivalry

data class ClubLocationProfile(
    val clubId: String,
    val city: String?,
    val state: String?,
    val region: String?,
    val country: String,
    val continent: String
)

object RivalryDiscoveryEngine {

    fun inferScope(
        first: ClubLocationProfile,
        second: ClubLocationProfile
    ): RivalryScope {
        require(first.clubId != second.clubId)

        return when {
            first.city != null &&
                first.city == second.city ->
                RivalryScope.LOCAL

            first.state != null &&
                first.state == second.state ->
                RivalryScope.STATE

            first.region != null &&
                first.region == second.region &&
                first.country == second.country ->
                RivalryScope.REGIONAL

            first.country == second.country ->
                RivalryScope.NATIONAL

            first.continent == second.continent ->
                RivalryScope.CONTINENTAL

            else ->
                RivalryScope.INTERNATIONAL
        }
    }

    fun shouldCreateRivalry(
        matchesPlayed: Int,
        finalsPlayed: Int,
        titleDecisions: Int
    ): Boolean {
        require(matchesPlayed >= 0)
        require(finalsPlayed >= 0)
        require(titleDecisions >= 0)

        return matchesPlayed >= 8 ||
            finalsPlayed >= 2 ||
            titleDecisions >= 1
    }

    fun initialScore(
        scope: RivalryScope,
        matchesPlayed: Int,
        finalsPlayed: Int,
        titleDecisions: Int
    ): Int {
        val geographicBase = when (scope) {
            RivalryScope.LOCAL -> 18
            RivalryScope.STATE -> 14
            RivalryScope.REGIONAL -> 10
            RivalryScope.NATIONAL -> 8
            RivalryScope.CONTINENTAL -> 6
            RivalryScope.INTERNATIONAL -> 4
        }

        return (
            geographicBase +
                matchesPlayed.coerceAtMost(20) +
                finalsPlayed * 5 +
                titleDecisions * 10
            ).coerceIn(0, 100)
    }
}
