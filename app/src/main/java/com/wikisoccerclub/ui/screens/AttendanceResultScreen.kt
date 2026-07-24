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
import com.wikisoccerclub.data.stadium.MatchAttendanceResult
import com.wikisoccerclub.data.stadium.Stadium
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun AttendanceResultScreen(
    result: MatchAttendanceResult?,
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
            title = "Público e renda",
            onBack = onBack
        )

        if (result == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    text = "Nenhum público calculado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            }
            return@Column
        }

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    text = "Público: ${result.totalAttendance}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ocupação: ${
                        (result.occupancyRate * 100).toInt()
                    }%",
                    color = WscGreenDark
                )
                Text(
                    text = "Renda bruta: ${result.grossTicketRevenue}",
                    color = WscGreenDark
                )
                Text(
                    text = "Custo da partida: ${result.maintenanceCost}",
                    color = WscGreenDark
                )
                Text(
                    text = "Renda líquida: ${result.netMatchRevenue}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    result.sectors,
                    key = { it.sectorId }
                ) { sectorResult ->
                    val sectorName =
                        stadium?.sectors
                            ?.firstOrNull {
                                it.id ==
                                    sectorResult.sectorId
                            }?.name
                            ?: sectorResult.sectorId

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = sectorName,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Público: ${sectorResult.attendance}",
                            color = WscGreenDark
                        )
                        Text(
                            text = "Renda: ${sectorResult.grossRevenue}",
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
