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
import com.wikisoccerclub.data.competition.GroupStageMatch
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun GroupStageFixturesScreen(
    matches: List<GroupStageMatch>,
    teamNames: Map<String, String>,
    groupNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Jogos dos grupos", onBack)

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    matches.sortedWith(
                        compareBy<GroupStageMatch> { it.round }
                            .thenBy { it.groupId }
                            .thenBy { it.id }
                    ),
                    key = { it.id }
                ) { match ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            "${groupNames[match.groupId] ?: match.groupId} • Rodada ${match.round}",
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold
                        )

                        val home = teamNames[match.homeTeamId]
                            ?: match.homeTeamId
                        val away = teamNames[match.awayTeamId]
                            ?: match.awayTeamId

                        Text(
                            if (match.played) {
                                "$home ${match.homeGoals ?: 0} x ${match.awayGoals ?: 0} $away"
                            } else {
                                "$home x $away"
                            },
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
