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
import com.wikisoccerclub.data.commercial.MerchandiseResult
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MerchandiseScreen(
    history: List<MerchandiseResult>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Produtos oficiais",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (history.isEmpty()) {
                Text(
                    text =
                        "Nenhum resultado comercial registrado.",
                    modifier =
                        Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        history,
                        key = { it.seasonYear }
                    ) { result ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text =
                                    "Temporada ${result.seasonYear}",
                                color = WscBlueDark,
                                fontWeight =
                                    FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Unidades vendidas: " +
                                        result.unitsSold,
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
                                    "Produção: " +
                                        result.productionCost,
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
                            Text(
                                text =
                                    "Popularidade: " +
                                        signed(
                                            result.popularityChange
                                        ),
                                color = WscGreenDark
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

private fun signed(value: Int): String =
    if (value > 0) "+$value"
    else value.toString()
