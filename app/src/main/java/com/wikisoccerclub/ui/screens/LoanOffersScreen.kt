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
import com.wikisoccerclub.data.transfer.LoanOffer
import com.wikisoccerclub.data.transfer.LoanStatus
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun LoanOffersScreen(
    offers: List<LoanOffer>,
    playerNames: Map<String, String>,
    clubNames: Map<String, String>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Empréstimos", onBack)

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
                            "${clubNames[offer.ownerClubId] ?: offer.ownerClubId} → " +
                                "${clubNames[offer.destinationClubId] ?: offer.destinationClubId}",
                            color = WscGreenDark
                        )
                        Text(
                            "Até ${offer.endYear}",
                            color = WscGreenDark
                        )
                        Text(
                            "Salário pago pelo destino: " +
                                "${offer.wagePercentagePaidByDestination}%",
                            color = WscGreenDark
                        )

                        offer.purchaseOption?.let {
                            Text(
                                "Opção de compra: $it",
                                color = WscBlueDark
                            )
                        }

                        if (offer.status == LoanStatus.PROPOSED) {
                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(
                                    onClick = { onAccept(offer.id) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Aceitar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = { onReject(offer.id) },
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
