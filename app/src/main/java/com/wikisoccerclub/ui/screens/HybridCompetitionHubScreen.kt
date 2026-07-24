package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun HybridCompetitionHubScreen(
    progress: HybridCompetitionProgress?,
    teamNames: Map<String, String>,
    onOpenGroups: () -> Unit,
    onOpenGroupFixtures: () -> Unit,
    onOpenKnockout: () -> Unit,
    onOpenChampion: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Competição", onBack)

        if (progress == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "Competição indisponível.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Fase atual",
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    phaseLabel(progress.phase),
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )

                progress.championTeamId?.let { championId ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Campeão: ${teamNames[championId] ?: championId}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        when (progress.phase) {
            HybridCompetitionPhase.GROUP_STAGE -> {
                Button(
                    onClick = onOpenGroups,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Classificação dos grupos")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = onOpenGroupFixtures,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Jogos da fase de grupos")
                }
            }

            HybridCompetitionPhase.KNOCKOUT -> {
                Button(
                    onClick = onOpenKnockout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Abrir mata-mata")
                }
            }

            HybridCompetitionPhase.COMPLETED -> {
                Button(
                    onClick = onOpenChampion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver campeão")
                }
            }
        }
    }
}

private fun phaseLabel(
    phase: HybridCompetitionPhase
): String = when (phase) {
    HybridCompetitionPhase.GROUP_STAGE -> "Fase de grupos"
    HybridCompetitionPhase.KNOCKOUT -> "Mata-mata"
    HybridCompetitionPhase.COMPLETED -> "Concluída"
}
