package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.stadium.Stadium
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun StadiumScreen(
    stadium: Stadium?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Estádio",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            if (stadium == null) {
                Text(
                    text = "Estádio não configurado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = stadium.name,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stadium.city,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Capacidade: ${stadium.capacity}",
                        color = WscGreenDark
                    )
                    Spacer(Modifier.height(10.dp))

                    FacilityLine(
                        "Gramado",
                        stadium.pitchQuality
                    )
                    FacilityLine(
                        "Iluminação",
                        stadium.lightingQuality
                    )
                    FacilityLine(
                        "Drenagem",
                        stadium.drainageQuality
                    )
                    FacilityLine(
                        "Segurança",
                        stadium.securityQuality
                    )
                    FacilityLine(
                        "Hospitalidade",
                        stadium.hospitalityQuality
                    )
                    FacilityLine(
                        "Estacionamento",
                        stadium.parkingQuality
                    )

                    Spacer(Modifier.height(10.dp))
                    Text(
                        text =
                            "Manutenção mensal: " +
                                stadium.maintenanceCostPerMonth,
                        color = WscBlueDark
                    )
                }
            }
        }
    }
}

@Composable
private fun FacilityLine(
    label: String,
    value: Int
) {
    Text(
        text = "$label: $value",
        color = WscGreenDark
    )
    LinearProgressIndicator(
        progress = { value / 100f },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
