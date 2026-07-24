package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.morale.TeamTalkTone
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun TeamTalkScreen(
    onSelectTone: (TeamTalkTone) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Conversa com o elenco",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Escolha o tom da conversa:",
                    color = WscBlueDark
                )

                TeamTalkTone.entries.forEach { tone ->
                    Button(
                        onClick = {
                            onSelectTone(tone)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(tone.name)
                    }
                }
            }
        }
    }
}
