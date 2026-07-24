package com.wikisoccerclub.data.supporters

enum class SupporterMood {
    FURIOUS,
    UNHAPPY,
    NEUTRAL,
    SATISFIED,
    EXCITED,
    ECSTATIC
}

enum class SupporterEventType {
    WIN,
    DRAW,
    DEFEAT,
    TITLE,
    RELEGATION,
    PROMOTION,
    BIG_SIGNING,
    PLAYER_SALE,
    TICKET_PRICE_CHANGE,
    STADIUM_UPGRADE,
    RIVALRY_WIN,
    RIVALRY_DEFEAT,
    BOARD_DECISION
}

enum class SocialPostType {
    MATCH_RESULT,
    TRANSFER,
    CLUB_NEWS,
    PLAYER_HIGHLIGHT,
    TROPHY,
    PROMOTION,
    COMMUNITY
}

data class SupporterProfile(
    val clubId: String,
    val totalFans: Long,
    val activeMembers: Int,
    val loyalty: Int,
    val satisfaction: Int,
    val engagement: Int,
    val internationalReach: Int,
    val averageAttendance: Int,
    val seasonTicketHolders: Int
) {
    val mood: SupporterMood
        get() = when {
            satisfaction < 15 -> SupporterMood.FURIOUS
            satisfaction < 30 -> SupporterMood.UNHAPPY
            satisfaction < 50 -> SupporterMood.NEUTRAL
            satisfaction < 70 -> SupporterMood.SATISFIED
            satisfaction < 90 -> SupporterMood.EXCITED
            else -> SupporterMood.ECSTATIC
        }
}

data class SupporterEvent(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val day: Int,
    val type: SupporterEventType,
    val importance: Int,
    val description: String
)

data class SupporterEventResult(
    val satisfactionChange: Int,
    val loyaltyChange: Int,
    val engagementChange: Int,
    val fanGrowth: Long,
    val membershipGrowth: Int,
    val message: String
)

data class SocialMediaProfile(
    val clubId: String,
    val followers: Long,
    val engagementRate: Double,
    val reputation: Int,
    val postsPublished: Int,
    val viralPosts: Int
)

data class SocialPost(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val day: Int,
    val type: SocialPostType,
    val title: String,
    val quality: Int,
    val relatedPlayerReputation: Int = 0,
    val relatedCompetitionReputation: Int = 0
)

data class SocialPostResult(
    val postId: String,
    val impressions: Long,
    val reactions: Long,
    val newFollowers: Long,
    val viral: Boolean,
    val reputationChange: Int
)

data class MembershipCampaign(
    val id: String,
    val clubId: String,
    val seasonYear: Int,
    val investment: Long,
    val durationDays: Int,
    val benefitQuality: Int,
    val startDay: Int
)

data class MembershipCampaignResult(
    val campaignId: String,
    val newMembers: Int,
    val grossRevenue: Long,
    val netRevenue: Long,
    val satisfactionChange: Int
)
