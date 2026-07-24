package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.transfer.AiTransferDecision
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun TransferAiMarketScreen(
    decisions: List<AiTransferDecision>,
    processing: Boolean,
    error: String?,
    playerNames: Map<String, String>,
    clubNames: Map<String, String>,
    onSimulate: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Mercado dos clubes",
            onBack = onBack
        )

        Button(
            onClick = onSimulate,
            enabled = !processing,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simular movimentações")
        }

        Spacer(Modifier.height(10.dp))

        if (processing) {
            CircularProgressIndicator()
            return@Column
        }

        error?.let {
            Text(
                text = it,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    decisions,
                    key = {
                        "${it.clubId}_${it.playerId}"
                    }
                ) { decision ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = playerNames[
                                decision.playerId
                            ] ?: decision.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Interessado: ${
                                clubNames[decision.clubId]
                                    ?: decision.clubId
                            }",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Oferta: ${
                                decision.transferValue
                            }",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Salário: ${
                                decision.proposedSalary
                            }",
                            color = WscGreenDark
                        )
                        Text(
                            text = decision.reason,
                            color = WscBlueDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
