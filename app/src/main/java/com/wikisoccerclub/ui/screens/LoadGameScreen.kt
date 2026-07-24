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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.save.GameSave
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscYellow

@Composable
fun LoadGameScreen(
    savedGame: State<GameSave?>,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(WscGreen).padding(18.dp)
    ) {
        WscTopBar("Carregar Jogo", onBack)

        val save = savedGame.value
        if (save == null) {
            WscWhiteCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Nenhuma carreira salva.",
                    modifier = Modifier.padding(18.dp),
                    color = WscBlueDark
                )
            }
        } else {
            WscWhiteCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(save.clubName, color = WscBlueDark, fontWeight = FontWeight.Bold)
                    Text("Treinador: ${save.managerName}", color = WscGreenDark)
                    Text("Temporada: ${save.season}", color = WscGreenDark)
                    Text("Evento atual: ${save.currentEvent}", color = WscGreenDark)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WscYellow,
                    contentColor = WscGreenDark
                )
            ) {
                Text("CONTINUAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}
