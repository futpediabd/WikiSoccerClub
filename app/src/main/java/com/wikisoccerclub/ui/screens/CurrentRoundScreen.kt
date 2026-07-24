package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CurrentRoundScreen(
    round: Int,
    teams: List<CompetitionTeam>,
    matches: List<CompetitionMatch>,
    onSimulateRound: () -> Unit,
    onOpenMatch: (String) -> Unit,
    onBack: () -> Unit
) {
    val names = teams.associate { it.id to it.name }
    val pending = matches.any { !it.played }

    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("$roundª rodada", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            LazyColumn {
                items(matches, key = { it.id }) { match ->
                    Row(
                        Modifier.fillMaxWidth()
                            .clickable(enabled = match.played) { onOpenMatch(match.id) }
                            .padding(12.dp)
                    ) {
                        Text(
                            names[match.homeTeamId].orEmpty(),
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            if (match.played) "  ${match.homeGoals} x ${match.awayGoals}  " else "  x  ",
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            names[match.awayTeamId].orEmpty(),
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    HorizontalDivider()
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onSimulateRound,
            enabled = pending,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = WscYellow,
                contentColor = WscGreenDark
            )
        ) {
            Text(
                if (pending) "SIMULAR RODADA" else "RODADA CONCLUÍDA",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
