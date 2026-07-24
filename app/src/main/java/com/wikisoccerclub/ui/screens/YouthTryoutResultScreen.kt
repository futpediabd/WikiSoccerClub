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
import com.wikisoccerclub.data.youth.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun YouthTryoutResultScreen(
    result: YouthTryoutResult?,
    onSignCandidate: (String) -> Unit,
    onRejectCandidate: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Resultado da peneira",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (result == null) {
                Text(
                    text =
                        "Nenhuma peneira realizada.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        result.candidates,
                        key = { it.id }
                    ) { candidate ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = candidate.name,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "${candidate.age} anos • ${candidate.nationality}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    candidate.specificPosition,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Overall ${candidate.overall} • Potencial ${candidate.potential}",
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Salário pedido: ${candidate.salaryRequest}",
                                color = WscBlueDark
                            )
                            Text(
                                text =
                                    "Status: ${candidate.status.label()}",
                                color = WscBlueDark
                            )

                            if (
                                candidate.status ==
                                YouthPlayerStatus.AVAILABLE
                            ) {
                                Spacer(
                                    Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        onSignCandidate(
                                            candidate.id
                                        )
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Contratar jogador"
                                    )
                                }

                                Spacer(
                                    Modifier.height(6.dp)
                                )

                                Button(
                                    onClick = {
                                        onRejectCandidate(
                                            candidate.id
                                        )
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                ) {
                                    Text("Dispensar")
                                }
                            }
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

private fun YouthPlayerStatus.label():
    String =
    when (this) {
        YouthPlayerStatus.AVAILABLE ->
            "Disponível"
        YouthPlayerStatus.SIGNED ->
            "Contratado"
        YouthPlayerStatus.REJECTED ->
            "Dispensado"
    }
