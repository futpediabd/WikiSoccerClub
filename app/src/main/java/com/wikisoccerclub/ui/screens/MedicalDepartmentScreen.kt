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
import com.wikisoccerclub.data.medical.Injury
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MedicalDepartmentScreen(
    injuries: List<Injury>,
    playerNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Departamento médico",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (injuries.isEmpty()) {
                Text(
                    text = "Nenhum jogador lesionado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        injuries,
                        key = { it.id }
                    ) { injury ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = playerNames[
                                    injury.playerId
                                ] ?: injury.playerId,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = injury.description,
                                color = WscGreenDark
                            )
                            Text(
                                text = "Gravidade: ${injury.severity}",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Retorno estimado: " +
                                    "${injury.remainingDays} dias",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Status: ${injury.status}",
                                color = WscBlueDark
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
