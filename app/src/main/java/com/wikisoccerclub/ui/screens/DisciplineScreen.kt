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
import com.wikisoccerclub.data.medical.PlayerDiscipline
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun DisciplineScreen(
    disciplines: List<PlayerDiscipline>,
    playerNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Cartões e suspensões",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    disciplines,
                    key = {
                        "${it.playerId}_${it.competitionId}"
                    }
                ) { discipline ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[
                                discipline.playerId
                            ] ?: discipline.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Amarelos: ${discipline.yellowCards}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Vermelhos: ${discipline.redCards}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Suspensão: " +
                                "${discipline.suspensionMatchesRemaining} jogo(s)",
                            color = WscBlueDark,
                            fontWeight =
                                if (
                                    discipline.suspensionMatchesRemaining > 0
                                ) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
