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
import com.wikisoccerclub.data.youth.YouthIntakeResult
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthIntakeScreen(
    result: YouthIntakeResult?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Nova geração da base",
            onBack = onBack
        )

        if (result == null) {
            WscWhiteCard(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    text =
                        "Nenhuma geração foi criada.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            }
            return
        }

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text =
                        "Temporada ${result.seasonYear}",
                    color = WscBlueDark,
                    fontWeight =
                        FontWeight.Bold
                )
                Text(
                    text =
                        "Jogadores: ${result.players.size}",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "Potencial médio: " +
                            String.format(
                                "%.1f",
                                result.averagePotential
                            ),
                    color = WscGreenDark
                )
                if (result.goldenGeneration) {
                    Text(
                        text = "Geração de ouro",
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
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
                    result.players,
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
                                player.nationality,
                            color = WscBlueDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
