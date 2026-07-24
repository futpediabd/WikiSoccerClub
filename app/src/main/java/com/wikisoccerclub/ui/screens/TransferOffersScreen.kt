package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.transfer.OfferStatus
import com.wikisoccerclub.data.transfer.TransferOffer
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun TransferOffersScreen(
    offers: List<TransferOffer>,
    playerNames: Map<String, String>,
    clubNames: Map<String, String>,
    error: String?,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit,
    onComplete: (String) -> Unit,
    onBack: () -> Unit
) {
    WscScreenBackground {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            WscTopBar(title = "Propostas de transferência", onBack = onBack)
            Spacer(Modifier.height(12.dp))
            error?.let { Text(it, color = WscBlueDark, fontWeight = FontWeight.Bold) }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(offers, key = { it.id }) { offer ->
                    WscWhiteCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text(playerNames[offer.playerId] ?: offer.playerId, color = WscBlueDark, fontWeight = FontWeight.Bold)
                            Text("${clubNames[offer.sellingClubId] ?: offer.sellingClubId} → ${clubNames[offer.buyingClubId] ?: offer.buyingClubId}", color = WscGreenDark)
                            Text("Valor: ${offer.value}", color = WscGreenDark)
                            Text("Status: ${offer.status}", color = WscGreenDark)
                            Spacer(Modifier.height(8.dp))
                            when (offer.status) {
                                OfferStatus.PENDING, OfferStatus.COUNTER -> Row {
                                    Button(onClick = { onAccept(offer.id) }, modifier = Modifier.weight(1f)) { Text("Aceitar") }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = { onReject(offer.id) }, modifier = Modifier.weight(1f)) { Text("Recusar") }
                                }
                                OfferStatus.ACCEPTED -> Button(
                                    onClick = { onComplete(offer.id) },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("Concluir transferência") }
                                OfferStatus.REJECTED -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}
