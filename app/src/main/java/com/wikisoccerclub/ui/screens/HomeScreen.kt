package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.ban.BanClub
import com.wikisoccerclub.data.calendar.MatchEvent
import com.wikisoccerclub.data.save.GameSave
import com.wikisoccerclub.ui.components.WscHeader
import com.wikisoccerclub.ui.components.WscPrimaryButton
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscTextMuted

@Composable
fun HomeScreen(
    savedGame: State<GameSave?>,
    club: BanClub?,
    nextMatch: MatchEvent?,
    onCompetition: () -> Unit,
    onSquad: () -> Unit,
    onTransferMarket: () -> Unit,
    onNextMatch: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val save = savedGame.value

    WscScreenBackground {
        Column(Modifier.fillMaxSize()) {
            WscHeader(
                title = save?.clubName ?: "WikiSoccerClub",
                subtitle = "Temporada ${save?.season ?: 2026} • Evento ${save?.currentEvent ?: 0}"
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                WscWhiteCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            "PRÓXIMO JOGO",
                            color = WscGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))

                        if (nextMatch == null) {
                            Text(
                                "Calendário concluído",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                "${nextMatch.home} x ${nextMatch.away}",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${nextMatch.competition} • ${nextMatch.round}",
                                color = WscTextMuted
                            )
                            Spacer(Modifier.height(12.dp))
                            WscPrimaryButton(
                                text = "INICIAR PRÓXIMO JOGO",
                                onClick = onNextMatch,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                WscWhiteCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp)) {
                        Text("CLUBE", color = WscGreenDark, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            club?.name ?: save?.clubName.orEmpty(),
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Força: ${club?.strength ?: 0}", color = WscTextMuted)
                        Text("Estádio: ${club?.stadiumName.orEmpty()}", color = WscTextMuted)
                        Text("Capacidade: ${club?.stadiumCapacity ?: 0}", color = WscTextMuted)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WscPrimaryButton(
                        text = "COMPETIÇÃO",
                        onClick = onCompetition,
                        modifier = Modifier.weight(1f)
                    )
                    WscPrimaryButton(
                        text = "ELENCO",
                        onClick = onSquad,
                        modifier = Modifier.weight(1f)
                    )
                }

                WscPrimaryButton(
                    text = "MERCADO DE TRANSFERÊNCIAS",
                    onClick = onTransferMarket,
                    modifier = Modifier.fillMaxWidth()
                )

                WscPrimaryButton(
                    text = "VOLTAR AO MENU",
                    onClick = onBackToMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
