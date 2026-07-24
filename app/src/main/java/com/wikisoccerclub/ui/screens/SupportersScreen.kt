package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.supporters.SupporterProfile
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun SupportersScreen(
    profile: SupporterProfile?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Torcedores",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            if (profile == null) {
                Text(
                    text =
                        "Perfil de torcedores indisponível.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                Column(
                    Modifier.padding(16.dp)
                ) {
                    Text(
                        text =
                            "Humor: ${profile.mood}",
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                    Text(
                        text =
                            "Total de torcedores: " +
                                profile.totalFans,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Sócios ativos: " +
                                profile.activeMembers,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Sócios com cadeira anual: " +
                                profile.seasonTicketHolders,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Público médio: " +
                                profile.averageAttendance,
                        color = WscBlueDark
                    )
                    Spacer(Modifier.height(10.dp))
                    Metric(
                        "Lealdade",
                        profile.loyalty
                    )
                    Metric(
                        "Satisfação",
                        profile.satisfaction
                    )
                    Metric(
                        "Engajamento",
                        profile.engagement
                    )
                    Metric(
                        "Alcance internacional",
                        profile.internationalReach
                    )
                }
            }
        }
    }
}

@Composable
private fun Metric(
    label: String,
    value: Int
) {
    Text(
        text = "$label: $value",
        color = WscGreenDark
    )
    LinearProgressIndicator(
        progress = {
            value.toFloat()
                .div(100f)
                .coerceIn(0f, 1f)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
