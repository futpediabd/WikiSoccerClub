package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.TopScorerRow
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun TopScorersScreen(
    competitionName: String,
    scorers: List<TopScorerRow>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(18.dp)
    ) {
        WscTopBar("Artilharia", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column {
                Text(
                    text = competitionName,
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )

                HorizontalDivider()

                if (scorers.isEmpty()) {
                    Text(
                        text = "Nenhum gol marcado.",
                        color = WscBlueDark,
                        modifier = Modifier.padding(18.dp)
                    )
                } else {
                    LazyColumn {
                        items(scorers, key = { it.playerId }) { scorer ->
                            ScorerRow(scorer)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScorerRow(scorer: TopScorerRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = "${scorer.position}",
            color = WscGreenDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.12f)
        )

        Column(Modifier.weight(.70f)) {
            Text(
                text = scorer.playerName,
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = scorer.teamName,
                color = WscGreenDark
            )
        }

        Text(
            text = "${scorer.goals}",
            color = WscBlueDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.18f)
        )
    }
}
