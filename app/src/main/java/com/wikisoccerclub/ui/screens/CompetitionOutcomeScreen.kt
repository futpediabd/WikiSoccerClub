package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.CompetitionOutcome
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CompetitionOutcomeScreen(
    outcome: CompetitionOutcome?,
    teamNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Fim da competição", onBack)

        if (outcome == null || !outcome.completed) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "A competição ainda não terminou.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "CAMPEÃO",
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    teamNames[outcome.championTeamId]
                        ?: outcome.championTeamId.orEmpty(),
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        OutcomeListCard(
            title = "Promovidos",
            ids = outcome.promotedTeamIds,
            teamNames = teamNames
        )

        Spacer(Modifier.height(10.dp))

        OutcomeListCard(
            title = "Rebaixados",
            ids = outcome.relegatedTeamIds,
            teamNames = teamNames
        )

        Spacer(Modifier.height(10.dp))

        OutcomeListCard(
            title = "Classificados",
            ids = outcome.continentalQualifiedTeamIds,
            teamNames = teamNames
        )
    }
}

@Composable
private fun OutcomeListCard(
    title: String,
    ids: List<String>,
    teamNames: Map<String, String>
) {
    WscWhiteCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                color = WscGreenDark,
                fontWeight = FontWeight.Bold
            )

            if (ids.isEmpty()) {
                Text(
                    "Nenhum time.",
                    color = WscBlueDark
                )
            } else {
                ids.forEachIndexed { index, id ->
                    Text(
                        "${index + 1}. ${teamNames[id] ?: id}",
                        color = WscBlueDark
                    )
                }
            }
        }
    }
}
