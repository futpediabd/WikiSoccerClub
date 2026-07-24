package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.CompetitionPlayer
import com.wikisoccerclub.data.competition.SubstitutionState
import com.wikisoccerclub.ui.components.WscPrimaryButton
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun SubstitutionScreen(
    starters: List<CompetitionPlayer>,
    reserves: List<CompetitionPlayer>,
    state: SubstitutionState,
    onSubstitute: (CompetitionPlayer, CompetitionPlayer) -> Unit
) {
    WscScreenBackground {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            WscTopBar(title = "Substituições")
            Spacer(Modifier.height(12.dp))

            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    text = "Substituições restantes: ${state.remaining}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(starters) { starter ->
                    reserves.firstOrNull()?.let { reserve ->
                        WscWhiteCard(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = starter.name,
                                    color = WscBlueDark,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Entra: ${reserve.name}",
                                    color = WscGreenDark
                                )
                                Spacer(Modifier.height(8.dp))
                                WscPrimaryButton(
                                    text = "Realizar substituição",
                                    onClick = { onSubstitute(starter, reserve) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
