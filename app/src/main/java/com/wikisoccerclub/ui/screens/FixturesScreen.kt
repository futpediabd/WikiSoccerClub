package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun FixturesScreen(
    competitionName: String,
    teams: List<CompetitionTeam>,
    matches: List<CompetitionMatch>,
    onOpenMatch: (String) -> Unit,
    onBack: () -> Unit
) {
    val names = teams.associate { it.id to it.name }

    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("$competitionName - Jogos", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            LazyColumn {
                items(matches, key = { it.id }) { match ->
                    Column(
                        Modifier.fillMaxWidth()
                            .clickable(enabled = match.played) { onOpenMatch(match.id) }
                    ) {
                        Text(
                            "${match.round}ª rodada",
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp, top = 10.dp)
                        )
                        Row(Modifier.fillMaxWidth().padding(12.dp)) {
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
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
