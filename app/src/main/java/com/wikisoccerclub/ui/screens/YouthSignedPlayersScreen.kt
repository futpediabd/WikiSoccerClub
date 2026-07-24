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
import com.wikisoccerclub.data.youth.YouthSignedPlayer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthSignedPlayersScreen(
    players: List<YouthSignedPlayer>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Contratados pela peneira",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (players.isEmpty()) {
                Text(
                    text =
                        "Nenhum jogador contratado.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        players,
                        key = { it.id }
                    ) { player ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = player.name,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "${player.age} anos • ${player.nationality}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    player.positionName,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Overall ${player.overall} • Potencial ${player.potential}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Contrato: ${player.contractYears} anos",
                                color = WscBlueDark
                            )
                            Text(
                                text =
                                    "Salário: ${player.salary}",
                                color = WscBlueDark
                            )
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
