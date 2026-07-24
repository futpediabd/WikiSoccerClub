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
import com.wikisoccerclub.data.supporters.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MembershipScreen(
    activeMembers: Int,
    history:
        List<Pair<MembershipCampaign, MembershipCampaignResult>>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Programa de sócios",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text =
                    "Sócios ativos: $activeMembers",
                modifier =
                    Modifier.padding(16.dp),
                color = WscBlueDark,
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (history.isEmpty()) {
                Text(
                    text =
                        "Nenhuma campanha realizada.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        history,
                        key = { it.first.id }
                    ) { item ->
                        val campaign = item.first
                        val result = item.second

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    "Campanha ${campaign.seasonYear}",
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Investimento: " +
                                        campaign.investment,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Novos sócios: " +
                                        result.newMembers,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Receita bruta: " +
                                        result.grossRevenue,
                                color = WscGreenDark
                            )
                            Text(
                                text =
                                    "Receita líquida: " +
                                        result.netRevenue,
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
