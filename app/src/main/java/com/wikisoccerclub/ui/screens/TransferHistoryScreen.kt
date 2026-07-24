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
import com.wikisoccerclub.data.transfer.CompletedTransfer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun TransferHistoryScreen(
    transfers: List<CompletedTransfer>,
    playerNames: Map<String, String>,
    clubNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Histórico de transferências", onBack)

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(transfers, key = { it.id }) { transfer ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[transfer.playerId]
                                ?: transfer.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )

                        val seller = transfer.sellingClubId?.let {
                            clubNames[it] ?: it
                        } ?: "Sem clube"

                        val buyer =
                            clubNames[transfer.buyingClubId]
                                ?: transfer.buyingClubId

                        Text(
                            text = "$seller → $buyer",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Valor: ${transfer.transferValue}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Temporada: ${
                                transfer.completedAtSeasonYear
                            }",
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
