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
import com.wikisoccerclub.data.scouting.ShortlistEntry
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun ShortlistScreen(
    entries: List<ShortlistEntry>,
    playerNames: Map<String, String>,
    onOpenPlayer: (String) -> Unit,
    onRemove: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Lista de observação",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    entries,
                    key = { it.playerId }
                ) { entry ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[
                                entry.playerId
                            ] ?: entry.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Prioridade: ${
                                entry.priority
                            }",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Adicionado em ${
                                entry.addedSeasonYear
                            }",
                            color = WscGreenDark
                        )
                        if (entry.note.isNotBlank()) {
                            Text(
                                text = entry.note,
                                color = WscBlueDark
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Row {
                            Button(
                                onClick = {
                                    onOpenPlayer(
                                        entry.playerId
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Abrir")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    onRemove(
                                        entry.playerId
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Remover")
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
