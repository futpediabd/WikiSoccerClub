package com.wikisoccerclub.ui.supporters

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.supporters.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SupporterUiState(
    val supporterProfile: SupporterProfile? = null,
    val socialProfile: SocialMediaProfile? = null,
    val events: List<SupporterEvent> =
        emptyList(),
    val socialHistory:
        List<Pair<SocialPost, SocialPostResult>> =
        emptyList(),
    val campaignHistory:
        List<Pair<MembershipCampaign, MembershipCampaignResult>> =
        emptyList(),
    val lastSupporterResult:
        SupporterEventResult? = null,
    val lastSocialResult:
        SocialPostResult? = null,
    val lastCampaignResult:
        MembershipCampaignResult? = null,
    val error: String? = null
)

class SupporterViewModel(
    private val repository: SupporterRepository =
        SupporterRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SupporterUiState())

    val uiState: StateFlow<SupporterUiState> =
        _uiState.asStateFlow()

    fun configure(
        supporterProfile: SupporterProfile,
        socialProfile: SocialMediaProfile
    ) {
        repository.saveSupporterProfile(
            supporterProfile
        )
        repository.saveSocialProfile(
            socialProfile
        )
        refresh(supporterProfile.clubId)
    }

    fun registerEvent(event: SupporterEvent) {
        val profile =
            repository.findSupporterProfile(
                event.clubId
            ) ?: return showError(
                "Perfil de torcedores não encontrado."
            )

        runCatching {
            SupporterEngine.applyEvent(
                profile = profile,
                event = event
            )
        }.onSuccess { (updated, result) ->
            repository.saveSupporterProfile(updated)
            repository.saveSupporterEvent(
                event,
                result
            )
            _uiState.value =
                _uiState.value.copy(
                    lastSupporterResult = result
                )
            refresh(event.clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun registerAttendance(
        clubId: String,
        attendance: Int,
        stadiumCapacity: Int
    ) {
        val profile =
            repository.findSupporterProfile(clubId)
                ?: return showError(
                    "Perfil de torcedores não encontrado."
                )

        runCatching {
            SupporterEngine.updateAttendance(
                profile,
                attendance,
                stadiumCapacity
            )
        }.onSuccess {
            repository.saveSupporterProfile(it)
            refresh(clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun publishPost(post: SocialPost) {
        val profile =
            repository.findSocialProfile(post.clubId)
                ?: return showError(
                    "Perfil social não encontrado."
                )

        runCatching {
            SocialMediaEngine.publish(
                profile = profile,
                post = post
            )
        }.onSuccess { (updated, result) ->
            repository.saveSocialProfile(updated)
            repository.saveSocialPost(post, result)
            _uiState.value =
                _uiState.value.copy(
                    lastSocialResult = result
                )
            refresh(post.clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun runMembershipCampaign(
        campaign: MembershipCampaign,
        monthlyFee: Long
    ) {
        val profile =
            repository.findSupporterProfile(
                campaign.clubId
            ) ?: return showError(
                "Perfil de torcedores não encontrado."
            )

        runCatching {
            MembershipEngine.runCampaign(
                profile = profile,
                campaign = campaign,
                monthlyFee = monthlyFee
            )
        }.onSuccess { (updated, result) ->
            repository.saveSupporterProfile(updated)
            repository.saveCampaign(campaign, result)
            _uiState.value =
                _uiState.value.copy(
                    lastCampaignResult = result
                )
            refresh(campaign.clubId)
        }.onFailure {
            showError(it.message)
        }
    }

    fun advanceDay(clubId: String) {
        val social =
            repository.findSocialProfile(clubId)
                ?: return

        repository.saveSocialProfile(
            SocialMediaEngine.organicDailyGrowth(
                social
            )
        )

        refresh(clubId)
    }

    private fun refresh(clubId: String) {
        _uiState.value =
            _uiState.value.copy(
                supporterProfile =
                    repository.findSupporterProfile(
                        clubId
                    ),
                socialProfile =
                    repository.findSocialProfile(
                        clubId
                    ),
                events =
                    repository.eventsByClub(clubId),
                socialHistory =
                    repository.socialHistory(clubId),
                campaignHistory =
                    repository.campaignHistory(clubId),
                error = null
            )
    }

    private fun showError(message: String?) {
        _uiState.value =
            _uiState.value.copy(
                error =
                    message ?: "Erro desconhecido."
            )
    }
}
