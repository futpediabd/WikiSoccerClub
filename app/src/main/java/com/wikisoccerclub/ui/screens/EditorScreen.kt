package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.wikisoccerclub.data.ban.BanClub
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun EditorScreen(clubs: List<BanClub>, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(WscGreen).padding(18.dp)
    ) {
        WscTopBar("Editor", onBack)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clubs, key = { it.sourceFile }) { club ->
                WscWhiteCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(club.name, color = WscBlueDark, fontWeight = FontWeight.Bold)
                        Text("${club.country} • ${club.city}", color = WscGreenDark)
                        Text("Nível ${club.level} • Força ${club.strength} • ${club.stars} estrelas", color = WscGreenDark)
                        Text("${club.players.size} jogadores importados", color = WscGreenDark)
                    }
                }
            }
        }
    }
}
