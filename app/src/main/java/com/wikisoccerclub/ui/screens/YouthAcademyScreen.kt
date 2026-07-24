package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.youth.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthAcademyScreen(
    academy: YouthAcademy?,
    summary: YouthAcademySummary?,
    players: List<YouthPlayer>,
    onPromotePlayer: (String) -> Unit,
    onReleasePlayer: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Categorias de base",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            if (academy == null) {
                Text(
                    text =
                        "Academia não configurada.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                Column(
                    Modifier.padding(16.dp)
                ) {
                    Text(
                        text =
                            "Nível ${academy.level}",
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                    Text(
                        text =
                            "Instalações: ${academy.facilities}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Treinadores: ${academy.coachingQuality}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Rede de captação: ${academy.recruitmentNetwork}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Foco: ${academy.focus}",
                        color = WscBlueDark
                    )
                    Text(
                        text =
                            "Custo mensal: ${academy.monthlyCost}",
                        color = WscGreenDark
                    )

                    summary?.let {
                        Spacer(
                            Modifier.height(8.dp)
                        )
                        Text(
                            text =
                                "Jogadores: ${it.totalPlayers}",
                            color = WscBlueDark
                        )
                        Text(
                            text =
                                "Média geral: " +
                                    String.format(
                                        "%.1f",
                                        it.averageOverall
                                    ),
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Média de potencial: " +
                                    String.format(
                                        "%.1f",
                                        it.averagePotential
                                    ),
                            color = WscGreenDark
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (players.isEmpty()) {
                Text(
                    text =
                        "Nenhum jovem cadastrado.",
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
                                    "${player.position} • ${player.age} anos",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Overall ${player.overall} • Potencial ${player.potential}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Status: ${player.status}",
                                color = WscBlueDark
                            )

                            if (
                                YouthAcademyEngine
                                    .canPromote(player)
                            ) {
                                Spacer(
                                    Modifier.height(8.dp)
                                )
                                Button(
                                    onClick = {
                                        onPromotePlayer(
                                            player.id
                                        )
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Promover ao profissional"
                                    )
                                }
                            }

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            Button(
                                onClick = {
                                    onReleasePlayer(
                                        player.id
                                    )
                                },
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {
                                Text("Dispensar jogador")
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
