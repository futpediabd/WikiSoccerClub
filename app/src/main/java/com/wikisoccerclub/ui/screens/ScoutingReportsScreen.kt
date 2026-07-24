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
import com.wikisoccerclub.data.scouting.ScoutingReport
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun ScoutingReportsScreen(
    reports: List<ScoutingReport>,
    playerNames: Map<String, String>,
    onOpenReport: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Relatórios de observação",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (reports.isEmpty()) {
                Text(
                    text = "Nenhum relatório disponível.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        reports,
                        key = { it.id }
                    ) { report ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    playerNames[report.playerId]
                                        ?: report.playerId,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Nível: ${report.observedCurrentAbility}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Potencial: ${report.observedPotential}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Confiança: ${report.confidence}%",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Recomendação: ${report.recommendationScore}",
                                color = WscBlueDark
                            )
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    onOpenReport(report.playerId)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Abrir relatório")
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
