package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.CompetitionProgress
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CompetitionStandingsScreen(
    progress: CompetitionProgress?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Classificação", onBack)

        if (progress == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "Classificação indisponível.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        Text(
            "Rodada atual: ${progress.currentRound}",
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(10.dp)
            ) {
                StandingHeader()

                progress.standings.entries.forEachIndexed { index, entry ->
                    HorizontalDivider()
                    Row(
                        Modifier
                            .width(720.dp)
                            .padding(vertical = 9.dp)
                    ) {
                        Cell("${index + 1}", 38)
                        Cell(entry.teamName, 220, true)
                        Cell("${entry.points}", 45, true)
                        Cell("${entry.played}", 45)
                        Cell("${entry.wins}", 45)
                        Cell("${entry.draws}", 45)
                        Cell("${entry.losses}", 45)
                        Cell("${entry.goalsFor}", 45)
                        Cell("${entry.goalsAgainst}", 45)
                        Cell("${entry.goalDifference}", 55)
                    }
                }
            }
        }
    }
}

@Composable
private fun StandingHeader() {
    Row(
        Modifier
            .width(720.dp)
            .padding(vertical = 8.dp)
    ) {
        Cell("#", 38, true)
        Cell("Time", 220, true)
        Cell("PTS", 45, true)
        Cell("J", 45, true)
        Cell("V", 45, true)
        Cell("E", 45, true)
        Cell("D", 45, true)
        Cell("GP", 45, true)
        Cell("GC", 45, true)
        Cell("SG", 55, true)
    }
}

@Composable
private fun RowScope.Cell(
    text: String,
    width: Int,
    bold: Boolean = false
) {
    Text(
        text = text,
        color = WscBlueDark,
        fontWeight = if (bold) FontWeight.Bold
        else FontWeight.Normal,
        modifier = Modifier.width(width.dp)
    )
}
