package com.wikisoccerclub.data.media

object NewsEngine {

    fun matchArticle(
        matchId: String,
        seasonYear: Int,
        day: Int,
        homeClubId: String,
        homeClubName: String,
        awayClubId: String,
        awayClubName: String,
        homeGoals: Int,
        awayGoals: Int,
        competitionId: String,
        competitionName: String,
        isFinal: Boolean = false
    ): NewsArticle {
        require(homeGoals >= 0 && awayGoals >= 0)

        val title = when {
            homeGoals > awayGoals ->
                "$homeClubName vence $awayClubName"
            awayGoals > homeGoals ->
                "$awayClubName vence $homeClubName"
            else ->
                "$homeClubName e $awayClubName empatam"
        }

        val importance = when {
            isFinal -> NewsImportance.BREAKING
            kotlin.math.abs(homeGoals - awayGoals) >= 4 ->
                NewsImportance.HIGH
            else -> NewsImportance.NORMAL
        }

        return NewsArticle(
            id = "noticia_jogo_$matchId",
            seasonYear = seasonYear,
            day = day,
            category = NewsCategory.MATCH,
            importance = importance,
            title = title,
            body =
                "$homeClubName $homeGoals x $awayGoals " +
                    "$awayClubName, pela competição " +
                    competitionName + ".",
            relatedClubIds =
                listOf(homeClubId, awayClubId),
            relatedCompetitionId = competitionId
        )
    }

    fun transferArticle(
        transferId: String,
        seasonYear: Int,
        day: Int,
        playerId: String,
        playerName: String,
        fromClubId: String?,
        fromClubName: String?,
        toClubId: String,
        toClubName: String,
        fee: Long
    ): NewsArticle {
        require(fee >= 0)

        val origin =
            fromClubName ?: "o mercado de agentes livres"

        return NewsArticle(
            id = "noticia_transferencia_$transferId",
            seasonYear = seasonYear,
            day = day,
            category = NewsCategory.TRANSFER,
            importance =
                if (fee >= 50_000_000L) {
                    NewsImportance.BREAKING
                } else if (fee >= 10_000_000L) {
                    NewsImportance.HIGH
                } else {
                    NewsImportance.NORMAL
                },
            title =
                "$toClubName anuncia $playerName",
            body =
                "$playerName deixa $origin e reforça " +
                    "$toClubName por $fee.",
            relatedClubIds =
                listOfNotNull(fromClubId, toClubId),
            relatedPlayerIds = listOf(playerId)
        )
    }

    fun injuryArticle(
        injuryId: String,
        seasonYear: Int,
        day: Int,
        clubId: String,
        playerId: String,
        playerName: String,
        description: String,
        recoveryDays: Int
    ): NewsArticle =
        NewsArticle(
            id = "noticia_lesao_$injuryId",
            seasonYear = seasonYear,
            day = day,
            category = NewsCategory.INJURY,
            importance = when {
                recoveryDays >= 90 ->
                    NewsImportance.HIGH
                recoveryDays >= 30 ->
                    NewsImportance.NORMAL
                else -> NewsImportance.LOW
            },
            title = "$playerName sofre lesão",
            body =
                "$playerName sofreu $description e ficará " +
                    "afastado por aproximadamente " +
                    "$recoveryDays dias.",
            relatedClubIds = listOf(clubId),
            relatedPlayerIds = listOf(playerId)
        )

    fun recordArticle(
        recordId: String,
        seasonYear: Int,
        day: Int,
        holderId: String,
        holderName: String,
        description: String
    ): NewsArticle =
        NewsArticle(
            id = "noticia_recorde_$recordId",
            seasonYear = seasonYear,
            day = day,
            category = NewsCategory.RECORD,
            importance = NewsImportance.HIGH,
            title = "$holderName estabelece novo recorde",
            body = description,
            relatedPlayerIds = listOf(holderId)
        )
}
