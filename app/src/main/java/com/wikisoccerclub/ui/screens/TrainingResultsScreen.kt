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
import com.wikisoccerclub.data.training.TrainingResult
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun TrainingResultsScreen(
    results: List<TrainingResult>,
    playerNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Resultado do treino",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (results.isEmpty()) {
                Text(
                    text = "Nenhum resultado disponível.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        results,
                        key = { it.playerId }
                    ) { result ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    playerNames[result.playerId]
                                        ?: result.playerId,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Overall: ${signed(result.overallChange)}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Físico: ${signed(result.fitnessChange)}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Ritmo: ${signed(result.sharpnessChange)}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Fadiga: ${signed(result.fatigueChange)}",
                                color = WscGreenDark
                            )
                            Text(
                                text = result.message,
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

private fun signed(value: Int): String =
    if (value > 0) "+$value" else value.toString()
