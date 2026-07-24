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
import com.wikisoccerclub.data.youth.YouthDevelopmentProjection
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthDevelopmentScreen(
    projections:
        List<YouthDevelopmentProjection>,
    playerNames:
        Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Projeção de desenvolvimento",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (projections.isEmpty()) {
                Text(
                    text =
                        "Nenhuma projeção disponível.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        projections,
                        key = { it.playerId }
                    ) { projection ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    playerNames[
                                        projection.playerId
                                    ] ?: projection.playerId,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Aos 18: ${projection.projectedOverallAt18}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Aos 21: ${projection.projectedOverallAt21}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Risco: ${projection.developmentRisk}%",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    projection.developmentLabel,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
