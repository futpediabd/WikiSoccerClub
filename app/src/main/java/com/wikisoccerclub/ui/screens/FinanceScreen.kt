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
import com.wikisoccerclub.data.finance.ClubFinance
import com.wikisoccerclub.data.finance.FinanceTransaction
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun FinanceScreen(
    finance: ClubFinance?,
    transactions: List<FinanceTransaction>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Finanças",
            onBack = onBack
        )

        finance?.let {
            WscWhiteCard(
                Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(
                        text = "Saldo: ${it.balance}",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Orçamento para transferências: " +
                            it.transferBudget,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Orçamento salarial: " +
                            it.wageBudget,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Folha atual: " +
                            it.monthlyPlayerWages,
                        color = WscGreenDark
                    )
                }
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
                    transactions,
                    key = { it.id }
                ) { transaction ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = transaction.description,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${transaction.month}/" +
                                transaction.seasonYear,
                            color = WscGreenDark
                        )
                        Text(
                            text = "Valor: ${transaction.amount}",
                            color = WscGreenDark
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
