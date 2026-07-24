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
import com.wikisoccerclub.data.match.CompletedMatchResult
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun PostMatchScreen(
    result: CompletedMatchResult?,
    homeName: String,
    awayName: String,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Fim de jogo", onBack)

        if (result == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "O resultado ainda não foi salvo.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "$homeName  ${result.homeGoals} x ${result.awayGoals}  $awayName",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Finalizações: ${result.homeShots} x ${result.awayShots}",
                    color = WscGreenDark
                )
                Text(
                    "No gol: ${result.homeShotsOnTarget} x ${result.awayShotsOnTarget}",
                    color = WscGreenDark
                )
                Text(
                    "Substituições: ${result.substitutions.size}",
                    color = WscGreenDark
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Eventos da partida",
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(Modifier.padding(12.dp)) {
                items(
                    result.events.asReversed(),
                    key = { it.id }
                ) { event ->
                    Text(
                        "${event.minute}'  ${event.description}",
                        color = WscBlueDark,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
