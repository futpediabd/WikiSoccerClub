package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.media.NewsArticle
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun NewsFeedScreen(
    articles: List<NewsArticle>,
    onOpenArticle: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Notícias",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (articles.isEmpty()) {
                Text(
                    text = "Nenhuma notícia disponível.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        articles,
                        key = { it.id }
                    ) { article ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenArticle(article.id)
                                }
                                .padding(14.dp)
                        ) {
                            Text(
                                text = article.title,
                                color = WscBlueDark,
                                fontWeight =
                                    if (article.isRead) {
                                        FontWeight.Normal
                                    } else {
                                        FontWeight.Bold
                                    }
                            )
                            Text(
                                text =
                                    "${article.category} • " +
                                        "Dia ${article.day}",
                                color = WscGreenDark
                            )
                            Text(
                                text = article.body,
                                color = WscGreenDark,
                                maxLines = 2
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
