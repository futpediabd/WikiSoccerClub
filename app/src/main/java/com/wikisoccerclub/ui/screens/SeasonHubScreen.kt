package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.season.Season
import com.wikisoccerclub.ui.components.WscPrimaryButton
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun SeasonHubScreen(
    season: Season,
    onNextSeason: () -> Unit
) {
    WscScreenBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            WscTopBar(title = "Temporada")

            WscWhiteCard(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Temporada atual",
                        color = WscGreenDark
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = season.year.toString(),
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            WscPrimaryButton(
                text = "Iniciar próxima temporada",
                onClick = onNextSeason,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
