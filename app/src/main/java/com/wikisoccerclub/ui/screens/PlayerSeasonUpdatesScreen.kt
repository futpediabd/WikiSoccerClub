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
import com.wikisoccerclub.data.season.PlayerSeasonUpdate
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun PlayerSeasonUpdatesScreen(
    updates: List<PlayerSeasonUpdate>,
    playerNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Atualização do elenco",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(updates, key = { it.playerId }) { update ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[update.playerId]
                                ?: update.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Idade: ${update.previousAge} → ${update.newAge}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Overall: ${update.previousOverall} → ${update.newOverall}",
                            color = WscGreenDark
                        )

                        if (update.contractExpired) {
                            Text(
                                text = "Contrato encerrado",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (update.retired) {
                            Text(
                                text = "Jogador aposentado",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
