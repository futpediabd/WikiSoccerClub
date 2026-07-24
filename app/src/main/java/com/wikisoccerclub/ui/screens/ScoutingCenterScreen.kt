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
import com.wikisoccerclub.data.scouting.ScoutProfile
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun ScoutingCenterScreen(
    scouts: List<ScoutProfile>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Central de observação",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (scouts.isEmpty()) {
                Text(
                    text = "Nenhum olheiro contratado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        scouts,
                        key = { it.id }
                    ) { scout ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = scout.name,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = scout.nationality,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Habilidade: ${scout.ability}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Potencial: ${scout.potentialJudgement}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Adaptação: ${scout.adaptability}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Salário: ${scout.monthlySalary}",
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
