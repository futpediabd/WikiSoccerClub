package com.wikisoccerclub.data.media

enum class NewsCategory {
    MATCH,
    TRANSFER,
    INJURY,
    SUSPENSION,
    BOARD,
    FINANCE,
    COMPETITION,
    RECORD,
    RIVALRY,
    NATIONAL_TEAM
}

enum class NewsImportance {
    LOW,
    NORMAL,
    HIGH,
    BREAKING
}

data class NewsArticle(
    val id: String,
    val seasonYear: Int,
    val day: Int,
    val category: NewsCategory,
    val importance: NewsImportance,
    val title: String,
    val body: String,
    val relatedClubIds: List<String> = emptyList(),
    val relatedPlayerIds: List<String> = emptyList(),
    val relatedCompetitionId: String? = null,
    val isRead: Boolean = false
)
