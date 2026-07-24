package com.wikisoccerclub.ui.media

import androidx.lifecycle.ViewModel
import com.wikisoccerclub.data.media.MediaRepository
import com.wikisoccerclub.data.media.NewsArticle
import com.wikisoccerclub.data.media.NewsCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MediaUiState(
    val articles: List<NewsArticle> = emptyList(),
    val unreadCount: Int = 0,
    val error: String? = null
)

class MediaViewModel(
    private val repository: MediaRepository = MediaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> = _uiState.asStateFlow()

    fun publish(article: NewsArticle) {
        repository.saveArticle(article)
        refresh()
    }

    fun markAsRead(articleId: String) {
        repository.markAsRead(articleId)
        refresh()
    }

    fun filterNews(
        category: NewsCategory? = null,
        clubId: String? = null
    ) {
        _uiState.value = _uiState.value.copy(
            articles = repository.articles(category, clubId),
            unreadCount = repository.unreadCount(),
            error = null
        )
    }

    private fun refresh() {
        _uiState.value = MediaUiState(
            articles = repository.articles(),
            unreadCount = repository.unreadCount()
        )
    }
}
