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
import com.wikisoccerclub.data.board.BoardObjective
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun BoardObjectivesScreen(
    objectives: List<BoardObjective>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Objetivos da diretoria",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    objectives,
                    key = { it.id }
                ) { objective ->
                    val progress = if (
                        objective.targetValue <= 0
                    ) {
                        0f
                    } else {
                        (
                            objective.currentValue.toFloat() /
                                objective.targetValue
                            ).coerceIn(0f, 1f)
                    }

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = objective.title,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = objective.description,
                            color = WscGreenDark
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "${objective.currentValue}/" +
                                "${objective.targetValue}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Status: ${objective.status}",
                            color = WscBlueDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
