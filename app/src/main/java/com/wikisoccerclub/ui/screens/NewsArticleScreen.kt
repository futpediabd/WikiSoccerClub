package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun NewsArticleScreen(
    article: NewsArticle?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Notícia",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                if (article == null) {
                    Text(
                        text = "Notícia não encontrada.",
                        color = WscBlueDark
                    )
                } else {
                    Text(
                        text = article.title,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text =
                            "${article.category} • " +
                                "Temporada ${article.seasonYear} • " +
                                "Dia ${article.day}",
                        color = WscGreenDark
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = article.body,
                        color = WscGreenDark
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text =
                            "Importância: ${article.importance}",
                        color = WscBlueDark
                    )
                }
            }
        }
    }
}
