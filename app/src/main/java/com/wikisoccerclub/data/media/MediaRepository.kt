package com.wikisoccerclub.data.media

class MediaRepository {

    private val articles = linkedMapOf<String, NewsArticle>()

    fun saveArticle(article: NewsArticle) {
        articles[article.id] = article
    }

    fun saveArticles(values: List<NewsArticle>) {
        values.forEach(::saveArticle)
    }

    fun articles(
        category: NewsCategory? = null,
        clubId: String? = null
    ): List<NewsArticle> =
        articles.values
            .filter {
                (category == null || it.category == category) &&
                    (clubId == null || clubId in it.relatedClubIds)
            }
            .sortedWith(
                compareByDescending<NewsArticle> { it.seasonYear }
                    .thenByDescending { it.day }
            )

    fun unreadCount(): Int =
        articles.values.count { !it.isRead }

    fun markAsRead(articleId: String) {
        val article = articles[articleId] ?: return
        articles[articleId] = article.copy(isRead = true)
    }
}
