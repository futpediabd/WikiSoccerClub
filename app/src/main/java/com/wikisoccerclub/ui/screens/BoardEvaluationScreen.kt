package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.board.BoardEvaluation
import com.wikisoccerclub.data.board.ClubReputation
import com.wikisoccerclub.data.board.ManagerReputation
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun BoardEvaluationScreen(
    evaluation: BoardEvaluation?,
    clubReputation: ClubReputation?,
    managerReputation: ManagerReputation?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Confiança da diretoria",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                if (evaluation == null) {
                    Text(
                        text = "Nenhuma avaliação disponível.",
                        color = WscBlueDark
                    )
                } else {
                    Text(
                        text = "${evaluation.confidence}%",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    LinearProgressIndicator(
                        progress = {
                            evaluation.confidence / 100f
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = evaluation.message,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Objetivos concluídos: " +
                            evaluation.completedObjectives,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Objetivos falhados: " +
                            evaluation.failedObjectives,
                        color = WscGreenDark
                    )
                    if (evaluation.dismissalRisk) {
                        Text(
                            text = "Risco de demissão",
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Reputações",
                    color = WscBlueDark,
                    fontWeight = FontWeight.Bold
                )
                clubReputation?.let {
                    Text(
                        text = "Clube nacional: " +
                            it.nationalReputation,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Clube continental: " +
                            it.continentalReputation,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Clube mundial: " +
                            it.globalReputation,
                        color = WscGreenDark
                    )
                }
                managerReputation?.let {
                    Text(
                        text = "Treinador: ${it.reputation}",
                        color = WscBlueDark
                    )
                    Text(
                        text = "Títulos: ${it.trophiesWon}",
                        color = WscGreenDark
                    )
                }
            }
        }
    }
}
