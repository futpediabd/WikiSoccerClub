package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.morale.PlayerMorale
import com.wikisoccerclub.data.morale.SquadAtmosphere
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SquadMoraleScreen(
    playerMorales: List<PlayerMorale>,
    playerNames: Map<String, String>,
    atmosphere: SquadAtmosphere?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Moral do elenco",
            onBack = onBack
        )

        atmosphere?.let {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(
                        text = "Ambiente: ${it.atmosphere}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Coesão: ${it.cohesion}",
                        color = WscGreenDark
                    )
                    Text(
                        text = "Apoio ao treinador: " +
                            it.managerSupport,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Jogadores insatisfeitos: " +
                            it.unhappyPlayers,
                        color = WscGreenDark
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    playerMorales,
                    key = { it.playerId }
                ) { morale ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[morale.playerId]
                                ?: morale.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Moral: ${morale.morale}",
                            color = WscGreenDark
                        )
                        LinearProgressIndicator(
                            progress = {
                                morale.morale / 100f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                        Text(
                            text = "Felicidade: " +
                                morale.happiness,
                            color = WscGreenDark
                        )
                        Text(
                            text = "Relação com o treinador: " +
                                morale.managerRelationship,
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
