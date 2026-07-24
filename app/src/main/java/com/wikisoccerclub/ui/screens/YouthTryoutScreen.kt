package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.youth.YouthPositionFilter
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthTryoutScreen(
    selectedPosition:
        YouthPositionFilter,
    cost: Long,
    onSelectPosition:
        (YouthPositionFilter) -> Unit,
    onRunTryout: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Peneira",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text =
                        "Escolha a posição da peneira",
                    color = WscBlueDark
                )

                Spacer(
                    Modifier.height(10.dp)
                )

                YouthPositionFilter.entries
                    .forEach { position ->
                        FilterChip(
                            selected =
                                selectedPosition ==
                                    position,
                            onClick = {
                                onSelectPosition(
                                    position
                                )
                            },
                            label = {
                                Text(
                                    position.label()
                                )
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        )
                    }

                Spacer(
                    Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Serão encontrados jogadores de 15 a 21 anos.",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "A maioria será do país do clube, mas jogadores estrangeiros também podem aparecer.",
                    color = WscGreenDark
                )
                Text(
                    text =
                        "Custo da peneira: $cost",
                    color = WscBlueDark
                )

                Spacer(
                    Modifier.height(12.dp)
                )

                Button(
                    onClick = onRunTryout,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {
                    Text("Realizar peneira")
                }
            }
        }
    }
}

private fun YouthPositionFilter.label():
    String =
    when (this) {
        YouthPositionFilter.ALL ->
            "Todas as posições"
        YouthPositionFilter.GOALKEEPER ->
            "Goleiros"
        YouthPositionFilter.DEFENDER ->
            "Defensores"
        YouthPositionFilter.MIDFIELDER ->
            "Meio-campistas"
        YouthPositionFilter.ATTACKER ->
            "Atacantes"
    }
