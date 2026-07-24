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
import com.wikisoccerclub.data.finance.SponsorshipOffer
import com.wikisoccerclub.data.finance.SponsorshipStatus
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SponsorshipOffersScreen(
    offers: List<SponsorshipOffer>,
    onAccept: (SponsorshipOffer) -> Unit,
    onReject: (SponsorshipOffer) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Patrocinadores",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    offers,
                    key = { it.id }
                ) { offer ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = offer.sponsorName,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Duração: ${offer.durationYears} temporadas",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Bônus inicial: ${offer.signingBonus}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Pagamento mensal: ${offer.monthlyPayment}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Bônus por título: ${offer.championshipBonus}",
                            color = WscGreenDark
                        )

                        if (offer.objectiveDescription.isNotBlank()) {
                            Text(
                                text = offer.objectiveDescription,
                                color = WscBlueDark
                            )
                        }

                        if (
                            offer.status ==
                                SponsorshipStatus.OFFERED
                        ) {
                            Spacer(Modifier.height(8.dp))
                            Row {
                                Button(
                                    onClick = {
                                        onAccept(offer)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Aceitar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onReject(offer)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Recusar")
                                }
                            }
                        } else {
                            Text(
                                text = "Status: ${offer.status}",
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
