package com.wikisoccerclub.data.supporters

class SupporterRepository {

    private val supporterProfiles =
        linkedMapOf<String, SupporterProfile>()

    private val socialProfiles =
        linkedMapOf<String, SocialMediaProfile>()

    private val supporterEvents =
        linkedMapOf<String, SupporterEvent>()

    private val supporterResults =
        mutableListOf<SupporterEventResult>()

    private val socialPosts =
        linkedMapOf<String, SocialPost>()

    private val socialResults =
        linkedMapOf<String, SocialPostResult>()

    private val campaigns =
        linkedMapOf<String, MembershipCampaign>()

    private val campaignResults =
        linkedMapOf<String, MembershipCampaignResult>()

    fun saveSupporterProfile(
        profile: SupporterProfile
    ) {
        supporterProfiles[profile.clubId] = profile
    }

    fun findSupporterProfile(
        clubId: String
    ): SupporterProfile? =
        supporterProfiles[clubId]

    fun saveSocialProfile(
        profile: SocialMediaProfile
    ) {
        socialProfiles[profile.clubId] = profile
    }

    fun findSocialProfile(
        clubId: String
    ): SocialMediaProfile? =
        socialProfiles[clubId]

    fun saveSupporterEvent(
        event: SupporterEvent,
        result: SupporterEventResult
    ) {
        supporterEvents[event.id] = event
        supporterResults += result
    }

    fun eventsByClub(
        clubId: String
    ): List<SupporterEvent> =
        supporterEvents.values
            .filter { it.clubId == clubId }
            .sortedWith(
                compareByDescending<SupporterEvent> {
                    it.seasonYear
                }.thenByDescending { it.day }
            )

    fun saveSocialPost(
        post: SocialPost,
        result: SocialPostResult
    ) {
        socialPosts[post.id] = post
        socialResults[post.id] = result
    }

    fun socialHistory(
        clubId: String
    ): List<Pair<SocialPost, SocialPostResult>> =
        socialPosts.values
            .filter { it.clubId == clubId }
            .mapNotNull { post ->
                socialResults[post.id]?.let {
                    post to it
                }
            }
            .sortedWith(
                compareByDescending<Pair<SocialPost, SocialPostResult>> {
                    it.first.seasonYear
                }.thenByDescending {
                    it.first.day
                }
            )

    fun saveCampaign(
        campaign: MembershipCampaign,
        result: MembershipCampaignResult
    ) {
        campaigns[campaign.id] = campaign
        campaignResults[campaign.id] = result
    }

    fun campaignHistory(
        clubId: String
    ): List<Pair<MembershipCampaign, MembershipCampaignResult>> =
        campaigns.values
            .filter { it.clubId == clubId }
            .mapNotNull { campaign ->
                campaignResults[campaign.id]?.let {
                    campaign to it
                }
            }
}
