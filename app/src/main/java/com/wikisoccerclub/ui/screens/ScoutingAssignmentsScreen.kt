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
import com.wikisoccerclub.data.scouting.ScoutingAssignment
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun ScoutingAssignmentsScreen(
    assignments: List<ScoutingAssignment>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Missões de observação",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (assignments.isEmpty()) {
                Text(
                    text = "Nenhuma missão criada.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        assignments,
                        key = { it.id }
                    ) { assignment ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = assignment.region.name,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Posição: ${assignment.positionGroup}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Idade: ${assignment.minimumAge}–${assignment.maximumAge}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Potencial mínimo: ${assignment.minimumPotential}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Duração: ${assignment.durationDays} dias",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Status: ${assignment.status}",
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
