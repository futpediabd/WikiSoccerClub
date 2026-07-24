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
import com.wikisoccerclub.data.stadium.StadiumFacility
import com.wikisoccerclub.data.stadium.StadiumUpgrade
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun StadiumUpgradesScreen(
    upgrades: List<StadiumUpgrade>,
    onStartUpgrade: (StadiumFacility) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Melhorias do estádio",
            onBack = onBack
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(14.dp),
                verticalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Iniciar nova melhoria",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )

                StadiumFacility.entries
                    .forEach { facility ->
                        Button(
                            onClick = {
                                onStartUpgrade(facility)
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(facility.name)
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
            if (upgrades.isEmpty()) {
                Text(
                    text = "Nenhuma obra em andamento.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        upgrades,
                        key = { it.id }
                    ) { upgrade ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = upgrade.facility.name,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Nível ${upgrade.currentLevel} → " +
                                        upgrade.targetLevel,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Custo: ${upgrade.cost}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Duração: ${upgrade.durationDays} dias",
                                color = WscGreenDark
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
