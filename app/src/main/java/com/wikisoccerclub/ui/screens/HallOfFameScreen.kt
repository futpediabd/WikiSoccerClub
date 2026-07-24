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
import com.wikisoccerclub.data.records.HallOfFameEntry
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun HallOfFameScreen(
    entries: List<HallOfFameEntry>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Hall da Fama",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (entries.isEmpty()) {
                Text(
                    text = "O Hall da Fama ainda está vazio.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        entries,
                        key = { it.id }
                    ) { entry ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = entry.subjectName,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Categoria: ${entry.type}",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Pontuação histórica: ${entry.score}",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Títulos: ${entry.titles}",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Entrada: ${entry.inductionYear}",
                                color = WscBlueDark
                            )
                            Text(
                                text = entry.description,
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
