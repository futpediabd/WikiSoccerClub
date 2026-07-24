package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun QualifiedTeamsScreen(
    teamIds: List<String>,
    teamNames: Map<String, String>,
    title: String = "Classificados",
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(title, onBack)

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                itemsIndexed(
                    teamIds,
                    key = { _, teamId -> teamId }
                ) { index, teamId ->
                    Text(
                        "${index + 1}. ${teamNames[teamId] ?: teamId}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
