package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun ScoutingReportDetailsScreen(
    report: ScoutingReport?,
    playerName: String,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Relatório do jogador",
            onBack = onBack
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            if (report == null) {
                Text(
                    text = "Relatório não encontrado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = playerName,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text =
                            "Nível observado: ${report.observedCurrentAbility}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Potencial observado: ${report.observedPotential}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Confiança do relatório: ${report.confidence}%",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Valor estimado: ${report.estimatedMarketValue}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Salário estimado: ${report.estimatedWageDemand}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Recomendação: ${report.recommendationScore}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Pontos fortes",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    report.strengths.forEach {
                        Text(
                            text = "• $it",
                            color = WscGreenDark
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Pontos fracos",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    report.weaknesses.forEach {
                        Text(
                            text = "• $it",
                            color = WscGreenDark
                        )
                    }
                }
            }
        }
    }
}
