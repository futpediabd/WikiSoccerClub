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
import com.wikisoccerclub.data.match.PlayerMatchUpdate
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun PostMatchPlayerUpdatesScreen(
    updates: List<PlayerMatchUpdate>,
    playerNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Situação dos jogadores", onBack)

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
                            .padding(12.dp)
                    ) {
                        Text(
                            playerNames[update.playerId] ?: update.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Energia final: ${update.finalEnergy}%",
                            color = WscGreenDark
                        )

                        if (update.yellowCards > 0) {
                            Text(
                                "Amarelos: ${update.yellowCards}",
                                color = WscGreenDark
                            )
                        }

                        if (update.redCards > 0) {
                            Text(
                                "Vermelhos: ${update.redCards}",
                                color = WscBlueDark
                            )
                        }

                        if (update.injured) {
                            Text(
                                "Lesionado por ${update.injuryRounds} rodada(s)",
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
