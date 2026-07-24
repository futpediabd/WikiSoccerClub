package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.records.SeasonAward
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SeasonAwardsScreen(
    seasonYear: Int,
    awards: List<SeasonAward>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Prêmios de $seasonYear",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    awards,
                    key = { it.id }
                ) { award ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = award.awardName,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = award.winnerName,
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        if (award.value > 0) {
                            Text(
                                text = "Valor: ${award.value}",
                                color = WscGreenDark
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
