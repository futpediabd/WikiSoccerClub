package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.training.WeeklyTrainingPlan
import com.wikisoccerclub.data.training.TrainingSession
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun TrainingPlanScreen(
    plan: WeeklyTrainingPlan?,
    onExecuteSession: (TrainingSession) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Plano de treinamento",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (plan == null) {
                Text(
                    text = "Nenhum plano semanal criado.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    item {
                        Column(
                            Modifier.padding(14.dp)
                        ) {
                            Text(
                                text =
                                    "Semana ${plan.weekNumber}",
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Dias de descanso: ${plan.restDays}",
                                color = WscGreenDark
                            )
                        }
                        HorizontalDivider()
                    }

                    items(
                        plan.sessions,
                        key = { it.id }
                    ) { session ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = session.focus.name,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text =
                                    "Dia ${session.day} • " +
                                        "${session.intensity} • " +
                                        "${session.durationMinutes} min",
                                color = WscGreenDark
                            )
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    onExecuteSession(session)
                                },
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {
                                Text("Realizar treino")
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
