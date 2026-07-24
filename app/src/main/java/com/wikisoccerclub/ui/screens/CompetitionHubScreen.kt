package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CompetitionHubScreen(
    competitionName: String,
    currentRound: Int,
    totalRounds: Int,
    completed: Boolean,
    onOpenCurrentRound: () -> Unit,
    onOpenStandings: () -> Unit,
    onOpenFixtures: () -> Unit,
    onOpenTopScorers: () -> Unit,
    onOpenDiscipline: () -> Unit,
    onOpenInjuries: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("Competição", onBack)
        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text(competitionName, color = WscBlueDark, fontWeight = FontWeight.Bold)
                Text(if (completed) "Competição encerrada" else "Rodada $currentRound de $totalRounds", color = WscGreenDark)
            }
        }
        Spacer(Modifier.height(12.dp))
        HubButton("RODADA ATUAL", onOpenCurrentRound)
        Spacer(Modifier.height(8.dp))
        HubButton("CLASSIFICAÇÃO", onOpenStandings)
        Spacer(Modifier.height(8.dp))
        HubButton("ARTILHARIA", onOpenTopScorers)
        Spacer(Modifier.height(8.dp))
        HubButton("DISCIPLINA", onOpenDiscipline)
        Spacer(Modifier.height(8.dp))
        HubButton("DEPARTAMENTO MÉDICO", onOpenInjuries)
        Spacer(Modifier.height(8.dp))
        HubButton("TODOS OS JOGOS", onOpenFixtures)
    }
}

@Composable
private fun HubButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = WscYellow, contentColor = WscGreenDark)
    ) { Text(text, fontWeight = FontWeight.Bold) }
}
