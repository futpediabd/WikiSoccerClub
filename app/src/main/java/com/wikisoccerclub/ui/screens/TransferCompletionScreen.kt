package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
fun TransferCompletionScreen(
    transfer: CompletedTransfer?,
    playerNames: Map<String, String>,
    clubNames: Map<String, String>,
    error: String?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Transferência concluída", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                if (error != null) {
                    Text(
                        text = error,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    return@Column
                }

                if (transfer == null) {
                    Text(
                        text = "Nenhuma transferência concluída.",
                        color = WscBlueDark
                    )
                    return@Column
                }

                Text(
                    text = playerNames[transfer.playerId]
                        ?: transfer.playerId,
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                transfer.sellingClubId?.let { sellerId ->
                    Text(
                        text = "Saiu de: ${
                            clubNames[sellerId] ?: sellerId
                        }",
                        color = WscGreenDark
                    )
                }

                Text(
                    text = "Novo clube: ${
                        clubNames[transfer.buyingClubId]
                            ?: transfer.buyingClubId
                    }",
                    color = WscGreenDark
                )
                Text(
                    text = "Valor: ${transfer.transferValue}",
                    color = WscGreenDark
                )
                Text(
                    text = "Contrato até ${
                        transfer.contract.endYear
                    }",
                    color = WscGreenDark
                )
            }
        }
    }
}
