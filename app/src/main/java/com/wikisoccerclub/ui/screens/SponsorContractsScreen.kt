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
import com.wikisoccerclub.data.commercial.SponsorContract
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SponsorContractsScreen(
    contracts: List<SponsorContract>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Contratos comerciais",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    contracts,
                    key = { it.id }
                ) { contract ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text =
                                contract.sponsorName,
                            color = WscBlueDark,
                            fontWeight =
                                FontWeight.Bold
                        )
                        Text(
                            text =
                                "${contract.category} • " +
                                    contract.status,
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Temporadas: " +
                                    "${contract.startSeason}–${contract.endSeason}",
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Valor anual: " +
                                    contract.fixedAnnualValue,
                            color = WscBlueDark
                        )
                        Text(
                            text =
                                "Total recebido: " +
                                    contract.totalReceived,
                            color = WscBlueDark,
                            fontWeight =
                                FontWeight.Bold
                        )

                        contract.objectives
                            .forEach { objective ->
                                Spacer(
                                    Modifier.height(8.dp)
                                )
                                Text(
                                    text =
                                        objective.type.name,
                                    color = WscGreenDark
                                )
                                val progress =
                                    if (
                                        objective.targetValue <= 0
                                    ) 0f
                                    else (
                                        objective.currentValue
                                            .toFloat() /
                                            objective.targetValue
                                        ).coerceIn(
                                            0f,
                                            1f
                                        )
                                LinearProgressIndicator(
                                    progress = {
                                        progress
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                )
                                Text(
                                    text =
                                        "${objective.currentValue}/" +
                                            objective.targetValue,
                                    color = WscGreenDark
                                )
                            }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
