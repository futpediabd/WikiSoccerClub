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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MatchDetailsScreen(
    match: CompetitionMatch,
    teams: List<CompetitionTeam>,
    onBack: () -> Unit
) {
    val teamNames = teams.associate { it.id to it.name }
    val playerNames = teams.flatMap { it.players }.associate { it.id to it.name }

    Column(
        Modifier.fillMaxSize().background(WscGreen).padding(18.dp)
    ) {
        WscTopBar("Detalhes do jogo", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    "${teamNames[match.homeTeamId]}  ${match.homeGoals} x ${match.awayGoals}  ${teamNames[match.awayTeamId]}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text("${match.round}ª rodada", color = WscGreenDark)
            }
        }

        WscWhiteCard(Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Column(Modifier.padding(12.dp)) {
                StatRow("Posse de bola", "${match.homePossession}%", "${match.awayPossession}%")
                StatRow("Finalizações", "${match.homeShots}", "${match.awayShots}")
                StatRow("No alvo", "${match.homeShotsOnTarget}", "${match.awayShotsOnTarget}")
            }
        }

        WscWhiteCard(Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Column {
                Text(
                    "EVENTOS",
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                HorizontalDivider()

                LazyColumn {
                    items(match.events, key = { it.id }) { event ->
                        val label = when (event.type) {
                            MatchEventType.GOAL -> "Gol"
                            MatchEventType.YELLOW_CARD -> "Cartão amarelo"
                            MatchEventType.RED_CARD -> "Cartão vermelho"
                            MatchEventType.INJURY -> "Contusão"
                        }
                        Column(Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(
                                "${event.minute}' • $label",
                                color = WscGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${playerNames[event.playerId] ?: "Jogador"} — ${teamNames[event.teamId]}",
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

@Composable
private fun StatRow(label: String, home: String, away: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp)) {
        Text(home, color = WscBlueDark, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End, modifier = Modifier.weight(.2f))
        Text(label, color = WscGreenDark, textAlign = TextAlign.Center,
            modifier = Modifier.weight(.6f))
        Text(away, color = WscBlueDark, fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.2f))
    }
}
