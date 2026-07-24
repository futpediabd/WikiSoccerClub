package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun GroupStageScreen(
    progress: GroupStageProgress?,
    onBack: () -> Unit
) {
    var selectedGroupId by remember(progress) {
        mutableStateOf(progress?.groups?.firstOrNull()?.id)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Fase de grupos", onBack)

        if (progress == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "Fase de grupos indisponível.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        Text(
            "Rodada ${progress.currentRound}",
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {
            progress.groups.forEach { group ->
                Text(
                    text = group.name,
                    color = if (selectedGroupId == group.id) {
                        WscYellow
                    } else {
                        androidx.compose.ui.graphics.Color.White
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                        .then(
                            Modifier
                        )
                )
            }
        }

        val selected = progress.groups.firstOrNull {
            it.id == selectedGroupId
        } ?: progress.groups.first()

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        TableCell("#", 32, true)
                        TableCell("Time", 170, true)
                        TableCell("PTS", 45, true)
                        TableCell("J", 35, true)
                        TableCell("SG", 45, true)
                    }
                }

                items(
                    selected.standings.entries,
                    key = { it.teamId }
                ) { entry ->
                    HorizontalDivider()
                    val position = selected.standings.entries
                        .indexOf(entry) + 1

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        TableCell("$position", 32)
                        TableCell(entry.teamName, 170, true)
                        TableCell("${entry.points}", 45, true)
                        TableCell("${entry.played}", 35)
                        TableCell("${entry.goalDifference}", 45)
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableCell(
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
