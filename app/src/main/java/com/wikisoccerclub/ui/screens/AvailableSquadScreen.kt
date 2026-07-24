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
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun AvailableSquadScreen(
    availability: TeamAvailability?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(18.dp)
    ) {
        WscTopBar("Disponibilidade do elenco", onBack)

        if (availability == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "Nenhum elenco carregado.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(18.dp)
                )
            }
            return@Column
        }

        Text(
            availability.team.name,
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            LazyColumn {
                items(
                    availability.players,
                    key = { it.player.id }
                ) { row ->
                    AvailabilityRow(row)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun AvailabilityRow(row: PlayerAvailability) {
    val statusText = when (row.status) {
        PlayerAvailabilityStatus.AVAILABLE -> "DISPONÍVEL"
        PlayerAvailabilityStatus.SUSPENDED -> "SUSPENSO"
        PlayerAvailabilityStatus.INJURED -> "LESIONADO"
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(Modifier.weight(.68f)) {
            Text(
                row.player.name,
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${row.player.position} • ${row.reason}",
                color = WscGreenDark
            )
        }

        Text(
            statusText,
            color = WscBlueDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.32f)
        )
    }
}
