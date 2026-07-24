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
import com.wikisoccerclub.data.transfer.SquadNeed
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SquadNeedsScreen(
    needs: List<SquadNeed>,
    clubNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Necessidades dos clubes",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    needs,
                    key = {
                        "${it.clubId}_${it.position}"
                    }
                ) { need ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = clubNames[need.clubId]
                                ?: need.clubId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Posição: ${need.position}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Prioridade: ${need.priority}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Overall mínimo: ${
                                need.minimumOverall
                            }",
                            color = WscGreenDark
                        )
                        need.maximumAge?.let {
                            Text(
                                text = "Idade máxima: $it",
                                color = WscGreenDark
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
