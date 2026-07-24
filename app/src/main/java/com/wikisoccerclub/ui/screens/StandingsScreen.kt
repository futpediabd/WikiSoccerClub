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
import com.wikisoccerclub.data.competition.StandingRow
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun StandingsScreen(
    competitionName: String,
    standings: List<StandingRow>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(18.dp)
    ) {
        WscTopBar(competitionName, onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column {
                StandingHeader()
                HorizontalDivider()
                LazyColumn {
                    items(standings, key = { it.teamId }) { row ->
                        StandingItem(row)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun StandingHeader() {
    Row(Modifier.fillMaxWidth().padding(8.dp)) {
        HeaderText("#", .08f)
        HeaderText("Clube", .38f)
        HeaderText("J", .09f)
        HeaderText("V", .09f)
        HeaderText("E", .09f)
        HeaderText("D", .09f)
        HeaderText("SG", .09f)
        HeaderText("P", .09f)
    }
}

@Composable
private fun StandingItem(row: StandingRow) {
    Row(Modifier.fillMaxWidth().padding(8.dp)) {
        ValueText("${row.position}", .08f)
        Text(
            text = row.teamName,
            color = WscBlueDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(.38f)
        )
        ValueText("${row.played}", .09f)
        ValueText("${row.wins}", .09f)
        ValueText("${row.draws}", .09f)
        ValueText("${row.losses}", .09f)
        ValueText("${row.goalDifference}", .09f)
        ValueText("${row.points}", .09f, true)
    }
}

@Composable
private fun HeaderText(text: String, weight: Float) {
    Text(
        text = text,
        color = WscGreenDark,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.weight(weight)
    )
}

@Composable
private fun ValueText(text: String, weight: Float, bold: Boolean = false) {
    Text(
        text = text,
        color = WscBlueDark,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.weight(weight)
    )
}
