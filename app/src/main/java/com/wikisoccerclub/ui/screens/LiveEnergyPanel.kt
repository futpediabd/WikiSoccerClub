package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.match.LiveTeamState
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun LiveEnergyPanel(
    homeTeam: LiveTeamState,
    awayTeam: LiveTeamState
) {
    WscWhiteCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "Energia média",
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            EnergyRow(homeTeam.teamName, homeTeam.averageEnergy)
            Spacer(Modifier.height(6.dp))
            EnergyRow(awayTeam.teamName, awayTeam.averageEnergy)
        }
    }
}

@Composable
private fun EnergyRow(
    teamName: String,
    energy: Int
) {
    Column {
        Text(
            "$teamName • $energy%",
            color = WscGreenDark
        )
        LinearProgressIndicator(
            progress = { energy.coerceIn(0, 100) / 100f },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
