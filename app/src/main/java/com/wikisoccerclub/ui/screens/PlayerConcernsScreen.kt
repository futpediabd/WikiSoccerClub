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
import com.wikisoccerclub.data.morale.PlayerConcern
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun PlayerConcernsScreen(
    concerns: List<PlayerConcern>,
    playerNames: Map<String, String>,
    onResolve: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Conversas com jogadores",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (concerns.isEmpty()) {
                Text(
                    text = "Nenhuma reclamação ativa.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        concerns,
                        key = { it.id }
                    ) { concern ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = playerNames[
                                    concern.playerId
                                ] ?: concern.playerId,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = concern.title,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = concern.description,
                                color = WscGreenDark
                            )
                            Text(
                                text = "Gravidade: " +
                                    concern.severity,
                                color = WscGreenDark
                            )
                            concern.deadlineDay?.let {
                                Text(
                                    text = "Prazo: dia $it",
                                    color = WscGreenDark
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Row {
                                Button(
                                    onClick = {
                                        onResolve(
                                            concern.id,
                                            true
                                        )
                                    },
                                    modifier =
                                        Modifier.weight(1f)
                                ) {
                                    Text("Resolver")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onResolve(
                                            concern.id,
                                            false
                                        )
                                    },
                                    modifier =
                                        Modifier.weight(1f)
                                ) {
                                    Text("Recusar")
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
