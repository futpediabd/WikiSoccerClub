package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.CompetitionLineup
import com.wikisoccerclub.data.competition.LineupValidation
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun LineupSummaryCard(
    lineup: CompetitionLineup,
    validation: LineupValidation
) {
    WscWhiteCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "Escalação ${lineup.formation}",
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${lineup.starters.size} titulares • ${lineup.substitutes.size} reservas",
                color = WscGreenDark
            )
            Text(
                validation.message,
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
