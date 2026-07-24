package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.calendar.MatchEvent
import com.wikisoccerclub.data.simulation.MatchResult
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscYellow

@Composable
fun MatchScreen(match: MatchEvent?, result: MatchResult?, onClose: () -> Unit) {
    Column(Modifier.fillMaxSize().background(WscGreen).padding(18.dp)) {
        Text("FIM DE JOGO", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text(
                    "${match?.competition ?: "Partida"} • ${match?.round.orEmpty()}",
                    color = WscGreenDark,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${result?.homeGoals ?: 0}  x  ${result?.awayGoals ?: 0}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    "${match?.home.orEmpty()}  •  ${match?.away.orEmpty()}",
                    color = WscGreenDark,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Divider()
                Spacer(Modifier.height(10.dp))
                Text("Finalizações: ${result?.homeShots ?: 0} x ${result?.awayShots ?: 0}", color = WscBlueDark)
                Text("Posse: ${result?.possessionHome ?: 50}% x ${result?.possessionAway ?: 50}%", color = WscBlueDark)
                Spacer(Modifier.height(10.dp))
                result?.events.orEmpty().forEach { event ->
                    Text(event, color = WscGreenDark)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WscYellow, contentColor = WscGreenDark)
        ) {
            Text("FECHAR", fontWeight = FontWeight.Bold)
        }
    }
}
