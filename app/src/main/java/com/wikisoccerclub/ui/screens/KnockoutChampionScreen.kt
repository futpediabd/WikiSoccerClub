package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.KnockoutCompetitionProgress
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun KnockoutChampionScreen(
    progress: KnockoutCompetitionProgress?,
    teamNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Campeão", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                if (progress?.completed == true &&
                    progress.championTeamId != null
                ) {
                    Text(
                        "CAMPEÃO",
                        color = WscGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        teamNames[progress.championTeamId]
                            ?: progress.championTeamId,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        "O campeão ainda não foi definido.",
                        color = WscBlueDark
                    )
                }
            }
        }
    }
}
