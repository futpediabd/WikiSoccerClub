package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.calendar.MatchEvent
import com.wikisoccerclub.data.squad.MatchLineup
import com.wikisoccerclub.data.squad.SquadPlayer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscYellow

@Composable
fun LineupScreen(
    match: MatchEvent?,
    lineup: MatchLineup,
    onBack: () -> Unit,
    onPlay: (MatchLineup) -> Unit
) {
    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("Escalação", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text(match?.competition ?: "Partida", color = WscGreenDark, fontWeight = FontWeight.Bold)
                Text("${match?.home.orEmpty()} x ${match?.away.orEmpty()}", color = WscBlueDark, fontWeight = FontWeight.Bold)
                Text(match?.round.orEmpty(), color = WscGreenDark)
            }
        }

        Spacer(Modifier.height(10.dp))
        Text("Titulares (${lineup.starters.size}/11)", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(lineup.starters, key = { it.id }) { PlayerRow(it) }
            item {
                Spacer(Modifier.height(8.dp))
                Text("Reservas (${lineup.bench.size}/7)", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
            }
            items(lineup.bench, key = { it.id }) { PlayerRow(it) }
        }

        Button(
            onClick = { onPlay(lineup) },
            enabled = lineup.isValid && match != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WscYellow, contentColor = WscGreenDark)
        ) {
            Text("INICIAR JOGO", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlayerRow(player: SquadPlayer) {
    WscWhiteCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(10.dp)) {
            Text(player.position, color = WscGreenDark, modifier = Modifier.weight(.22f))
            Text(player.name, color = WscBlueDark, fontWeight = FontWeight.Bold, modifier = Modifier.weight(.58f))
            Text("${player.overall}", color = WscGreenDark, fontWeight = FontWeight.Bold, modifier = Modifier.weight(.2f))
        }
    }
}
