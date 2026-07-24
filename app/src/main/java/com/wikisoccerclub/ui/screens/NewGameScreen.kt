package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.ban.BanClub
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscYellow

@Composable
fun NewGameScreen(
    clubs: List<BanClub>,
    onBack: () -> Unit,
    onCreateGame: (String, BanClub) -> Unit
) {
    var managerName by remember { mutableStateOf("") }
    var selectedClub by remember { mutableStateOf<BanClub?>(clubs.firstOrNull()) }

    Column(
        modifier = Modifier.fillMaxSize().background(WscGreen).padding(18.dp)
    ) {
        WscTopBar("Novo Jogo", onBack)

        WscWhiteCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nome do treinador", color = WscBlueDark, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = managerName,
                    onValueChange = { managerName = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Digite seu nome") }
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Escolha o clube", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clubs, key = { it.sourceFile }) { club ->
                WscWhiteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedClub = club }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedClub?.sourceFile == club.sourceFile,
                            onClick = { selectedClub = club }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(club.name, color = WscBlueDark, fontWeight = FontWeight.Bold)
                            Text("${club.country} • ${club.city}", color = WscGreenDark)
                            Text("Força ${club.strength} • ${club.stars} estrelas", color = WscGreenDark)
                        }
                    }
                }
            }
        }

        Button(
            onClick = { selectedClub?.let { onCreateGame(managerName, it) } },
            enabled = selectedClub != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WscYellow,
                contentColor = WscGreenDark
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("COMEÇAR CARREIRA", fontWeight = FontWeight.Bold)
        }
    }
}
