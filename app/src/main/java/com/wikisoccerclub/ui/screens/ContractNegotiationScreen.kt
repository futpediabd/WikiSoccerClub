package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.transfer.ContractNegotiationStatus
import com.wikisoccerclub.data.transfer.ContractOffer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun ContractNegotiationScreen(
    offers: List<ContractOffer>,
    playerNames: Map<String, String>,
    onAccept: (ContractOffer) -> Unit,
    onReject: (ContractOffer) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Contratos", onBack)

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(offers, key = { it.id }) { offer ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            playerNames[offer.playerId]
                                ?: offer.playerId,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Contrato: ${offer.startYear}–${offer.endYear}",
                            color = WscGreenDark
                        )
                        Text(
                            "Salário mensal: ${offer.monthlySalary}",
                            color = WscGreenDark
                        )
                        Text(
                            "Bônus de assinatura: ${offer.signingBonus}",
                            color = WscGreenDark
                        )

                        if (offer.status ==
                            ContractNegotiationStatus.PENDING ||
                            offer.status ==
                            ContractNegotiationStatus.COUNTER
                        ) {
                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(
                                    onClick = { onAccept(offer) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Aceitar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = { onReject(offer) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Recusar")
                                }
                            }
                        } else {
                            Text(
                                "Status: ${offer.status}",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
