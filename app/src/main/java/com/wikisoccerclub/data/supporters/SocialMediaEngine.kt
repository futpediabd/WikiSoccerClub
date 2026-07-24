package com.wikisoccerclub.data.supporters

import kotlin.math.roundToLong
import kotlin.random.Random

object SocialMediaEngine {

    fun publish(
        profile: SocialMediaProfile,
        post: SocialPost,
        random: Random = Random.Default
    ): Pair<SocialMediaProfile, SocialPostResult> {
        require(post.quality in 0..100)
        require(post.relatedPlayerReputation in 0..100)
        require(post.relatedCompetitionReputation in 0..100)

        val contentMultiplier = when (post.type) {
            SocialPostType.MATCH_RESULT -> 1.00
            SocialPostType.TRANSFER -> 1.20
            SocialPostType.CLUB_NEWS -> 0.85
            SocialPostType.PLAYER_HIGHLIGHT -> 1.10
            SocialPostType.TROPHY -> 1.60
            SocialPostType.PROMOTION -> 1.35
            SocialPostType.COMMUNITY -> 0.95
        }

        val baseReach =
            profile.followers *
                (
                    0.25 +
                        post.quality / 120.0 +
                        profile.reputation / 180.0
                    ) *
                contentMultiplier

        val subjectBonus =
            1.0 +
                post.relatedPlayerReputation / 250.0 +
                post.relatedCompetitionReputation / 300.0

        val variation =
            random.nextDouble(0.82, 1.25)

        val impressions = (
            baseReach *
                subjectBonus *
                variation
            ).roundToLong()
            .coerceAtLeast(0)

        val reactions = (
            impressions *
                profile.engagementRate
                    .coerceIn(0.01, 0.50)
            ).roundToLong()

        val viralThreshold =
            profile.followers * 1.40

        val viral =
            impressions >= viralThreshold &&
                post.quality >= 75

        val newFollowers = (
            reactions *
                if (viral) 0.20 else 0.05
            ).roundToLong()

        val reputationChange = when {
            viral && post.type ==
                SocialPostType.TROPHY -> 3
            viral -> 2
            post.quality >= 80 -> 1
            post.quality < 25 -> -1
            else -> 0
        }

        val result = SocialPostResult(
            postId = post.id,
            impressions = impressions,
            reactions = reactions,
            newFollowers = newFollowers,
            viral = viral,
            reputationChange = reputationChange
        )

        val updated = profile.copy(
            followers =
                profile.followers +
                    newFollowers,
            reputation =
                (profile.reputation +
                    reputationChange)
                    .coerceIn(0, 100),
            postsPublished =
                profile.postsPublished + 1,
            viralPosts =
                profile.viralPosts +
                    if (viral) 1 else 0
        )

        return updated to result
    }

    fun organicDailyGrowth(
        profile: SocialMediaProfile
    ): SocialMediaProfile {
        val growth = (
            profile.followers *
                (
                    0.00005 +
                        profile.reputation / 2_000_000.0
                    )
            ).roundToLong()

        return profile.copy(
            followers =
                profile.followers +
                    growth.coerceAtLeast(0)
        )
    }
}
