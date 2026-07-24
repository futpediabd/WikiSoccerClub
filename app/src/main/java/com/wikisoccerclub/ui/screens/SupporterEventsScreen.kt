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
import com.wikisoccerclub.data.supporters.SupporterEvent
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SupporterEventsScreen(
    events: List<SupporterEvent>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Reação da torcida",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (events.isEmpty()) {
                Text(
                    text =
                        "Nenhum evento registrado.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        events,
                        key = { it.id }
                    ) { event ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = event.type.name,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Temporada ${event.seasonYear} • " +
                                        "Dia ${event.day}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    event.description,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Importância: " +
                                        event.importance,
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
