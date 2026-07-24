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
import com.wikisoccerclub.data.rivalry.RivalrySummary
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun RivalryDetailsScreen(
    summary: RivalrySummary?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Detalhes da rivalidade",
            onBack = onBack
        )

        if (summary == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    text = "Rivalidade não encontrada.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            }
            return@Column
        }

        val rivalry = summary.rivalry

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    text =
                        "${rivalry.clubAName} x " +
                            rivalry.clubBName,
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text =
                        "Intensidade: ${rivalry.intensity}",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "Confrontos: ${rivalry.matches}",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "${rivalry.clubAName}: " +
                            rivalry.clubAWins,
                    color = WscGreenDark
                )
                Text(
                    text = "Empates: ${rivalry.draws}",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "${rivalry.clubBName}: " +
                            rivalry.clubBWins,
                    color = WscGreenDark
                )
                Text(
                    text =
                        "Gols: ${rivalry.clubAGoals} x " +
                            rivalry.clubBGoals,
                    color = WscBlueDark
                )
                Text(
                    text =
                        "Finais disputadas: " +
                            rivalry.finalsPlayed,
                    color = WscGreenDark
                )
                Text(
                    text =
                        "Decisões de título: " +
                            rivalry.titleDecisions,
                    color = WscGreenDark
                )
                summary.biggestClubAWin?.let {
                    Text(
                        text = "Maior vitória A: $it",
                        color = WscGreenDark
                    )
                }
                summary.biggestClubBWin?.let {
                    Text(
                        text = "Maior vitória B: $it",
                        color = WscGreenDark
                    )
                }
                Text(
                    text =
                        "Maior público: " +
                            summary.highestAttendance,
                    color = WscGreenDark
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    summary.recentMatches,
                    key = { it.matchId }
                ) { match ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text =
                                "${match.clubAName} " +
                                    "${match.clubAGoals} x " +
                                    "${match.clubBGoals} " +
                                    match.clubBName,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text =
                                "${match.competitionName} • " +
                                    match.seasonYear,
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
