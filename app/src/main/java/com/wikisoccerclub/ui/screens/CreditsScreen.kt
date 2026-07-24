package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.R
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun CreditsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(WscGreen).padding(18.dp)
    ) {
        WscTopBar("Créditos", onBack)

        WscWhiteCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.wsc_logo_oficial),
                    contentDescription = "Logo WikiSoccerClub",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text("WikiSoccerClub", color = WscBlueDark, fontWeight = FontWeight.Bold)
                Text("Versão 0.2.0 — Etapa 02", color = WscGreenDark)
                Spacer(Modifier.height(8.dp))
                Text("Projeto oficial do jogo de gestão de futebol WikiSoccerClub.", color = WscGreenDark)
            }
        }
    }
}
