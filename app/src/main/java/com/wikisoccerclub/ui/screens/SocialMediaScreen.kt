package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.supporters.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SocialMediaScreen(
    profile: SocialMediaProfile?,
    history:
        List<Pair<SocialPost, SocialPostResult>>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Redes sociais",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(14.dp)
            ) {
                if (profile == null) {
                    Text(
                        text =
                            "Perfil social indisponível.",
                        color = WscBlueDark
                    )
                } else {
                    Text(
                        text =
                            "Seguidores: ${profile.followers}",
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                    Text(
                        text =
                            "Engajamento: " +
                                String.format(
                                    "%.1f%%",
                                    profile.engagementRate * 100
                                ),
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Publicações: " +
                                profile.postsPublished,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Publicações virais: " +
                                profile.viralPosts,
                        color = WscBlueDark
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (history.isEmpty()) {
                Text(
                    text =
                        "Nenhuma publicação registrada.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        history,
                        key = { it.first.id }
                    ) { item ->
                        val post = item.first
                        val result = item.second

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = post.title,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "${post.type} • Dia ${post.day}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Impressões: ${result.impressions}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Reações: ${result.reactions}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Novos seguidores: " +
                                        result.newFollowers,
                                color = WscBlueDark
                            )
                            if (result.viral) {
                                Text(
                                    text = "Publicação viral",
                                    color = WscBlueDark,
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
