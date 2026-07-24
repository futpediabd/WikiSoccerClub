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
import com.wikisoccerclub.data.medical.Injury
import com.wikisoccerclub.data.medical.PlayerDiscipline
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

data class PlayerAvailabilityItem(
    val playerId: String,
    val playerName: String,
    val injury: Injury?,
    val discipline: PlayerDiscipline?
)

@Composable
fun PlayerAvailabilityScreen(
    players: List<PlayerAvailabilityItem>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Disponibilidade do elenco",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    players,
                    key = { it.playerId }
                ) { item ->
                    val suspended =
                        item.discipline
                            ?.suspensionMatchesRemaining
                            ?.let { it > 0 } == true
                    val injured =
                        item.injury != null
                    val available =
                        !injured && !suspended

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = item.playerName,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = when {
                                injured ->
                                    "Lesionado: ${item.injury?.remainingDays} dias"
                                suspended ->
                                    "Suspenso: ${item.discipline?.suspensionMatchesRemaining} jogo(s)"
                                else -> "Disponível"
                            },
                            color = if (available) {
                                WscGreenDark
                            } else {
                                WscBlueDark
                            },
                            fontWeight =
                                if (available) {
                                    FontWeight.Normal
                                } else {
                                    FontWeight.Bold
                                }
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
