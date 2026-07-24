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
import com.wikisoccerclub.data.competition.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun KnockoutBracketScreen(
    progress: KnockoutCompetitionProgress?,
    teamNames: Map<String, String>,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar("Mata-mata", onBack)

        if (progress == null) {
            WscWhiteCard(Modifier.fillMaxWidth()) {
                Text(
                    "Chaveamento indisponível.",
                    color = WscBlueDark,
                    modifier = Modifier.padding(16.dp)
                )
            }
            return@Column
        }

        Text(
            roundLabel(progress.currentRound),
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                val currentTies = progress.ties.filter {
                    it.round == progress.currentRound
                }

                items(currentTies, key = { it.id }) { tie ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        val home = teamNames[tie.homeTeamId]
                            ?: tie.homeTeamId
                        val away = teamNames[tie.awayTeamId]
                            ?: tie.awayTeamId

                        Text(
                            "$home x $away",
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )

                        tie.firstLegHomeGoals?.let { homeGoals ->
                            Text(
                                "Ida: $homeGoals x " +
                                    "${tie.firstLegAwayGoals ?: 0}",
                                color = WscGreenDark
                            )
                        }

                        tie.secondLegHomeGoals?.let { secondHome ->
                            Text(
                                "Volta: $secondHome x " +
                                    "${tie.secondLegAwayGoals ?: 0}",
                                color = WscGreenDark
                            )
                        }

                        if (tie.completed) {
                            Text(
                                "Classificado: ${
                                    teamNames[tie.winnerTeamId]
                                        ?: tie.winnerTeamId.orEmpty()
                                }",
                                color = WscGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun roundLabel(
    round: KnockoutRoundType
): String = when (round) {
    KnockoutRoundType.PRELIMINARY -> "Fase preliminar"
    KnockoutRoundType.ROUND_OF_128 -> "128 avos"
    KnockoutRoundType.ROUND_OF_64 -> "64 avos"
    KnockoutRoundType.ROUND_OF_32 -> "32 avos"
    KnockoutRoundType.ROUND_OF_16 -> "Oitavas de final"
    KnockoutRoundType.QUARTER_FINAL -> "Quartas de final"
    KnockoutRoundType.SEMI_FINAL -> "Semifinal"
    KnockoutRoundType.FINAL -> "Final"
}
