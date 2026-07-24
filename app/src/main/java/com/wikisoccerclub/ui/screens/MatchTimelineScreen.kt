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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.MatchEvent
import com.wikisoccerclub.data.competition.SubstitutionState
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscOutline

@Composable
fun MatchTimelineScreen(
    events: List<MatchEvent>,
    substitutions: SubstitutionState
) {
    WscScreenBackground {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            WscTopBar(title = "Linha do tempo")
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(events) { event ->
                    WscWhiteCard(Modifier.fillMaxWidth()) {
                        Text(
                            text = "${event.minute}' - ${event.type}",
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Substituições",
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(substitutions.substitutions) { substitution ->
                    WscWhiteCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = "${substitution.minute}'",
                                color = WscGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = WscOutline
                            )
                            Text(
                                text = "${substitution.playerOutId} → ${substitution.playerInId}",
                                color = WscBlueDark
                            )
                        }
                    }
                }
            }
        }
    }
}
