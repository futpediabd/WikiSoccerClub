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
import com.wikisoccerclub.data.competition.CompetitionSeasonRecord
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CompetitionHistoryScreen(
    records: List<CompetitionSeasonRecord>,
    teamNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Histórico", onBack)

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    records,
                    key = {
                        "${it.competitionId}_${it.season}"
                    }
                ) { record ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            "Temporada ${record.season}",
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Campeão: ${
                                teamNames[record.championTeamId]
                                    ?: record.championTeamId
                            }",
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        record.runnerUpTeamId?.let { runnerUpId ->
                            Text(
                                "Vice: ${
                                    teamNames[runnerUpId]
                                        ?: runnerUpId
                                }",
                                color = WscBlueDark
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
