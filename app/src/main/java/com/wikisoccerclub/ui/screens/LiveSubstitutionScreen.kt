package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.match.LivePlayerState
import com.wikisoccerclub.data.match.LiveTeamState
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun LiveSubstitutionScreen(
    team: LiveTeamState,
    onSubstitute: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var selectedOut by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Substituições", onBack)

        Text(
            "${team.teamName} • Restantes: ${team.substitutionsRemaining}",
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        if (message.isNotBlank()) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    message,
                    color = WscBlueDark,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            PlayerColumn(
                title = "Em campo",
                players = team.starters,
                selectedId = selectedOut,
                modifier = Modifier.weight(1f),
                onClick = { selectedOut = it.player.id }
            )

            Spacer(Modifier.width(8.dp))

            PlayerColumn(
                title = "Banco",
                players = team.bench,
                selectedId = null,
                modifier = Modifier.weight(1f),
                onClick = { playerIn ->
                    val outId = selectedOut
                    if (outId == null) {
                        message = "Selecione primeiro quem vai sair."
                    } else {
                        onSubstitute(outId, playerIn.player.id)
                        message = "Substituição registrada."
                        selectedOut = null
                    }
                }
            )
        }
    }
}

@Composable
private fun PlayerColumn(
    title: String,
    players: List<LivePlayerState>,
    selectedId: String?,
    modifier: Modifier,
    onClick: (LivePlayerState) -> Unit
) {
    WscWhiteCard(modifier.fillMaxHeight()) {
        Column(Modifier.fillMaxSize()) {
            Text(
                title,
                color = WscBlueDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )

            HorizontalDivider()

            LazyColumn {
                items(players, key = { it.player.id }) { state ->
                    val prefix = if (state.player.id == selectedId) "✓ " else ""

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onClick(state) }
                            .padding(10.dp)
                    ) {
                        Text(
                            prefix + state.player.name,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${state.player.position} • Energia ${state.energy}",
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
