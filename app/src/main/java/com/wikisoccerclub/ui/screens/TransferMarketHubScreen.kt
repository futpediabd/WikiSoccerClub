package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.ui.components.WscPrimaryButton
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscTopBar

@Composable
fun TransferMarketHubScreen(
    onOpenWindow: () -> Unit,
    onOpenOffers: () -> Unit,
    onOpenContracts: () -> Unit,
    onOpenLoans: () -> Unit,
    onOpenAiMarket: () -> Unit,
    onOpenHistory: () -> Unit,
    onBack: () -> Unit
) {
    WscScreenBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            WscTopBar(title = "Mercado de transferências", onBack = onBack)
            WscPrimaryButton("JANELA E JOGADORES DISPONÍVEIS", onOpenWindow, Modifier.fillMaxWidth())
            WscPrimaryButton("PROPOSTAS", onOpenOffers, Modifier.fillMaxWidth())
            WscPrimaryButton("CONTRATOS", onOpenContracts, Modifier.fillMaxWidth())
            WscPrimaryButton("EMPRÉSTIMOS", onOpenLoans, Modifier.fillMaxWidth())
            WscPrimaryButton("MERCADO DOS CLUBES", onOpenAiMarket, Modifier.fillMaxWidth())
            WscPrimaryButton("HISTÓRICO", onOpenHistory, Modifier.fillMaxWidth())
        }
    }
}
