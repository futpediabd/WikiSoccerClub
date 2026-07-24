package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.rivalry.ClubRivalry
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun RivalriesScreen(
    rivalries: List<ClubRivalry>,
    onSelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Rivalidades",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (rivalries.isEmpty()) {
                Text(
                    text = "Nenhuma rivalidade registrada.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        rivalries,
                        key = { it.id }
                    ) { rivalry ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(rivalry.id)
                                }
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    "${rivalry.clubAName} x " +
                                        rivalry.clubBName,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Abrangência: ${rivalry.scope}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Intensidade: ${rivalry.intensity}",
                                color = WscGreenDark
                            )
                            LinearProgressIndicator(
                                progress = {
                                    rivalry.score / 100f
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )
                            Text(
                                text =
                                    "${rivalry.matches} confrontos",
                                color = WscGreenDark
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
