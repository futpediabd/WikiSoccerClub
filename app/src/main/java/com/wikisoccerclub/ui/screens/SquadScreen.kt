package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.squad.SquadPlayer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun SquadScreen(players: List<SquadPlayer>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        WscTopBar("Elenco (${players.size}/35)", onBack)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(players, key = { it.id }) { player ->
                WscWhiteCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(player.position, color = WscGreenDark, fontWeight = FontWeight.Bold, modifier = Modifier.weight(.22f))
                        Text(player.name, color = WscBlueDark, fontWeight = FontWeight.Bold, modifier = Modifier.weight(.58f))
                        Text("${player.overall}", color = WscGreenDark, fontWeight = FontWeight.Bold, modifier = Modifier.weight(.2f))
                    }
                }
            }
        }
    }
}
