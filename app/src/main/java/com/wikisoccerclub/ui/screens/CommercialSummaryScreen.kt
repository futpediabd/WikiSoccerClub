package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.commercial.CommercialSummary
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CommercialSummaryScreen(
    summary: CommercialSummary?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Resumo comercial",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            if (summary == null) {
                Text(
                    text =
                        "Resumo comercial indisponível.",
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
                            "Contratos ativos: " +
                                summary.activeContracts.size,
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                    Spacer(
                        Modifier.height(8.dp)
                    )
                    Text(
                        text =
                            "Patrocínios anuais: " +
                                summary.annualSponsorIncome,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Bônus de objetivos: " +
                                summary.objectiveBonuses,
                        color = WscGreenDark
                    )
                    Text(
                        text =
                            "Produtos oficiais: " +
                                summary.merchandiseRevenue,
                        color = WscGreenDark
                    )
                    Spacer(
                        Modifier.height(10.dp)
                    )
                    Text(
                        text =
                            "Receita comercial total: " +
                                summary.totalCommercialRevenue,
                        color = WscBlueDark,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}
