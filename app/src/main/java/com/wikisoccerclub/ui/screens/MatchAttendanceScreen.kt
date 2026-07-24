package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.stadium.MatchAttendanceResult
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MatchAttendanceScreen(
    result: MatchAttendanceResult?,
    stadiumCapacity: Int,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Público e renda",
            onBack = onBack
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            if (result == null) {
                Text(
                    text =
                        "Os dados de público ainda não foram calculados.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text =
                            "Público: ${result.attendance}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text =
                            "Capacidade: $stadiumCapacity",
                        color = WscGreenDark
                    )
                    LinearProgressIndicator(
                        progress = {
                            result.occupancyRate
                                .toFloat()
                                .coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text =
                            "Popular: ${result.popularTickets}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Padrão: ${result.standardTickets}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Premium: ${result.premiumTickets}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "VIP: ${result.vipTickets}",
                        color = WscGreenDark
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text =
                            "Renda bruta: ${result.grossRevenue}",
                        color = WscBlueDark
                    )
                    Text(
                        text =
                            "Custos: ${result.operatingCost}",
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Renda líquida: ${result.netRevenue}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
