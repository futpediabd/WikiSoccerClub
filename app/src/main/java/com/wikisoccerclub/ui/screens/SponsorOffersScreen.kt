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
import com.wikisoccerclub.data.commercial.SponsorOffer
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SponsorOffersScreen(
    offers: List<SponsorOffer>,
    onAccept: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Ofertas de patrocínio",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (offers.isEmpty()) {
                Text(
                    text =
                        "Nenhuma oferta disponível.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
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
                                text =
                                    offer.sponsorName,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    offer.category.name,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Valor anual: " +
                                        offer.fixedAnnualValue,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Bônus de assinatura: " +
                                        offer.signingBonus,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Duração: " +
                                        "${offer.durationSeasons} temporada(s)",
                                color = WscBlueDark
                            )
                            Spacer(
                                Modifier.height(8.dp)
                            )
                            Button(
                                onClick = {
                                    onAccept(offer.id)
                                },
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {
                                Text("Aceitar oferta")
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
