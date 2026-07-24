package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.transfer.TransferTarget
import com.wikisoccerclub.data.transfer.TransferWindow
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark

@Composable
fun TransferWindowScreen(
    windows: List<TransferWindow>,
    targets: List<TransferTarget>,
    onBack: () -> Unit
) {
    WscScreenBackground {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            WscTopBar(title = "Janela de transferências", onBack = onBack)
            Spacer(Modifier.height(12.dp))

            WscWhiteCard(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    windows.forEach { window ->
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = window.type.toString(),
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (window.isOpen) "Aberta" else "Fechada",
                                color = WscGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(targets) { target ->
                    WscWhiteCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = "Jogador: ${target.playerId}",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Clube: ${target.clubId}",
                                color = WscGreenDark
                            )
                            Text(
                                text = "Valor: ${target.askingPrice}",
                                color = WscGreenDark
                            )
                            if (target.availableForLoan) {
                                Text(
                                    text = "Disponível por empréstimo",
                                    color = WscBlueDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
