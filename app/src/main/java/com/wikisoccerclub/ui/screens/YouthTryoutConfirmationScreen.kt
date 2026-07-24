package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.youth.YouthTryoutAvailability
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthTryoutConfirmationScreen(
    clubBalance: Long,
    availability:
        YouthTryoutAvailability?,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Confirmar peneira",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text =
                        "Saldo do clube: $clubBalance",
                    color = WscBlueDark,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                if (availability == null) {
                    Text(
                        text =
                            "Carregando informações da peneira.",
                        color = WscGreenDark
                    )
                } else {
                    Text(
                        text =
                            "Custo: ${availability.cost}",
                        color = WscBlueDark
                    )
                    Text(
                        text =
                            availability.message,
                        color = WscGreenDark
                    )

                    if (
                        availability.available &&
                        clubBalance >=
                        availability.cost
                    ) {
                        Spacer(
                            Modifier.height(12.dp)
                        )

                        Button(
                            onClick = onConfirm,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Confirmar e pagar"
                            )
                        }
                    } else if (
                        clubBalance <
                        availability.cost
                    ) {
                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Saldo insuficiente.",
                            color = WscBlueDark,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
