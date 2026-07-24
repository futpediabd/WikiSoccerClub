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
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.InjuryRow
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun InjuriesScreen(rows: List<InjuryRow>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("Departamento médico", onBack)
        WscWhiteCard(Modifier.fillMaxWidth()) {
            if (rows.isEmpty()) {
                Text("Nenhum jogador lesionado.", color = WscBlueDark, modifier = Modifier.padding(18.dp))
            } else {
                LazyColumn {
                    items(rows, key = { it.playerId }) { row ->
                        Column(Modifier.fillMaxWidth().padding(12.dp)) {
                            Text(row.playerName, color = WscBlueDark, fontWeight = FontWeight.Bold)
                            Text(row.teamName, color = WscGreenDark)
                            Text("Retorno previsto: rodada ${row.returnRound}", color = WscBlueDark)
                            Text("${row.remainingRounds} rodada(s) restante(s)", color = WscGreenDark, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
