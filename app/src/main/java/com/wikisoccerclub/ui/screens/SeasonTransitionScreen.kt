package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.season.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SeasonTransitionScreen(
    state: SeasonTransitionUiState,
    teamNames: Map<String, String>,
    competitionNames: Map<String, String>,
    onStartNextSeason: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Fim da temporada",
            onBack = onBack
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Temporada ${state.currentSeason.year}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (state.currentSeason.finished) {
                        "Temporada encerrada"
                    } else {
                        "Temporada em andamento"
                    },
                    color = WscGreenDark
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        state.error?.let {
            Text(
                text = it,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (state.loading) {
            CircularProgressIndicator()
            return@Column
        }

        val transition = state.transition
        if (transition == null) {
            Button(
                onClick = onStartNextSeason,
                enabled = state.currentSeason.finished,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Processar próxima temporada")
            }
            return@Column
        }

        Text(
            text = "Temporada ${transition.newYear} preparada",
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    transition.movements,
                    key = {
                        "${it.teamId}_${it.fromCompetitionId}_${it.toCompetitionId}"
                    }
                ) { movement ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = teamNames[movement.teamId]
                                ?: movement.teamId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = movementLabel(
                                movement = movement,
                                competitionNames = competitionNames
                            ),
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun movementLabel(
    movement: PromotionRelegationMovement,
    competitionNames: Map<String, String>
): String {
    val destination = competitionNames[movement.toCompetitionId]
        ?: movement.toCompetitionId

    return when (movement.type) {
        MovementType.PROMOTION -> "Promovido para $destination"
        MovementType.RELEGATION -> "Rebaixado para $destination"
    }
}
