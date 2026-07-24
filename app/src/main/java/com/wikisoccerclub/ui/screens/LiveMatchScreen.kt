package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.match.*
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun LiveMatchScreen(
    state: LiveMatchState,
    homeName: String,
    awayName: String,
    onAdvance: () -> Unit,
    onTogglePause: () -> Unit,
    onChangeSpeed: (MatchSpeed) -> Unit,
    onOpenSubstitutions: () -> Unit,
    onResumeAfterHalfTime: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "${state.currentMinute}'",
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$homeName  ${state.homeScore} x ${state.awayScore}  $awayName",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Finalizações ${state.homeShots} x ${state.awayShots}",
                    color = WscGreenDark
                )
                Text(
                    "No gol ${state.homeShotsOnTarget} x ${state.awayShotsOnTarget}",
                    color = WscGreenDark
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth()) {
            SpeedButton(
                text = "1x",
                selected = state.speed == MatchSpeed.NORMAL,
                modifier = Modifier.weight(1f)
            ) { onChangeSpeed(MatchSpeed.NORMAL) }

            Spacer(Modifier.width(6.dp))

            SpeedButton(
                text = "2x",
                selected = state.speed == MatchSpeed.FAST,
                modifier = Modifier.weight(1f)
            ) { onChangeSpeed(MatchSpeed.FAST) }

            Spacer(Modifier.width(6.dp))

            SpeedButton(
                text = "4x",
                selected = state.speed == MatchSpeed.VERY_FAST,
                modifier = Modifier.weight(1f)
            ) { onChangeSpeed(MatchSpeed.VERY_FAST) }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(Modifier.padding(12.dp)) {
                items(
                    state.events.asReversed(),
                    key = { it.id }
                ) { event ->
                    Text(
                        "${event.minute}'  ${event.description}",
                        color = WscBlueDark,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    HorizontalDivider()
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        when {
            state.pausedForSubstitution -> {
                Button(
                    onClick = onOpenSubstitutions,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WscYellow,
                        contentColor = WscGreenDark
                    )
                ) {
                    Text("ABRIR SUBSTITUIÇÕES", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(6.dp))

                Button(
                    onClick = onResumeAfterHalfTime,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("INICIAR SEGUNDO TEMPO")
                }
            }

            state.finished -> {
                Button(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PARTIDA ENCERRADA")
                }
            }

            else -> {
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onAdvance,
                        enabled = !state.paused,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("AVANÇAR")
                    }

                    Spacer(Modifier.width(6.dp))

                    Button(
                        onClick = onTogglePause,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (state.paused) "CONTINUAR" else "PAUSAR")
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeedButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) WscYellow else WscGreenDark,
            contentColor = if (selected) WscGreenDark else androidx.compose.ui.graphics.Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}
