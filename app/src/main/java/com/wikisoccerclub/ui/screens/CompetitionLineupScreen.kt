package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun CompetitionLineupScreen(
    lineup: CompetitionLineup,
    availability: TeamAvailability?,
    validation: LineupValidation,
    onChangeStyle: (TacticalStyle) -> Unit,
    onRegenerate: () -> Unit,
    onMoveToStarters: (CompetitionPlayer) -> Unit,
    onMoveToSubstitutes: (CompetitionPlayer) -> Unit,
    onRemove: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(18.dp)
    ) {
        WscTopBar("Escalação", onBack)

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text(
                    "Formação ${lineup.formation}",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Titulares ${lineup.starters.size}/11 • Reservas ${lineup.substitutes.size}/7",
                    color = WscGreenDark
                )
                Text(
                    validation.message,
                    color = if (validation.valid) WscGreenDark else WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {
            StyleButton(
                "DEF",
                lineup.tacticalStyle == TacticalStyle.DEFENSIVE,
                Modifier.weight(1f)
            ) { onChangeStyle(TacticalStyle.DEFENSIVE) }

            Spacer(Modifier.width(5.dp))

            StyleButton(
                "EQU",
                lineup.tacticalStyle == TacticalStyle.BALANCED,
                Modifier.weight(1f)
            ) { onChangeStyle(TacticalStyle.BALANCED) }

            Spacer(Modifier.width(5.dp))

            StyleButton(
                "OFE",
                lineup.tacticalStyle == TacticalStyle.OFFENSIVE,
                Modifier.weight(1f)
            ) { onChangeStyle(TacticalStyle.OFFENSIVE) }
        }

        Spacer(Modifier.height(8.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = WscGreenDark,
            contentColor = WscYellow
        ) {
            listOf("Titulares", "Reservas", "Disponíveis").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (selectedTab) {
                0 -> PlayerList(
                    players = lineup.starters,
                    actionLabel = "BANCO",
                    onAction = onMoveToSubstitutes,
                    onRemove = onRemove
                )

                1 -> PlayerList(
                    players = lineup.substitutes,
                    actionLabel = "TITULAR",
                    onAction = onMoveToStarters,
                    onRemove = onRemove
                )

                else -> {
                    val available = availability
                        ?.availablePlayers
                        ?.map { it.player }
                        .orEmpty()
                        .filterNot { lineup.contains(it.id) }

                    PlayerList(
                        players = available,
                        actionLabel = "ADICIONAR",
                        onAction = { player ->
                            if (lineup.starters.size < 11) {
                                onMoveToStarters(player)
                            } else {
                                onMoveToSubstitutes(player)
                            }
                        },
                        onRemove = {}
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onRegenerate,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = WscYellow,
                contentColor = WscGreenDark
            )
        ) {
            Text("ESCALAÇÃO AUTOMÁTICA", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlayerList(
    players: List<CompetitionPlayer>,
    actionLabel: String,
    onAction: (CompetitionPlayer) -> Unit,
    onRemove: (String) -> Unit
) {
    if (players.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            Text(
                "Nenhum jogador nesta lista.",
                color = WscBlueDark
            )
        }
        return
    }

    LazyColumn {
        items(players, key = { it.id }) { player ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(Modifier.weight(.58f)) {
                    Text(
                        player.name,
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${player.position} • FIN ${player.finishing}",
                        color = WscGreenDark
                    )
                }

                Text(
                    actionLabel,
                    color = WscGreenDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onAction(player) }
                        .padding(6.dp)
                )

                if (onRemove !== {}) {
                    Text(
                        "REMOVER",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onRemove(player.id) }
                            .padding(6.dp)
                    )
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
private fun StyleButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) WscYellow else WscGreenDark,
            contentColor = if (selected) WscGreenDark else androidx.compose.ui.graphics.Color.White
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}
