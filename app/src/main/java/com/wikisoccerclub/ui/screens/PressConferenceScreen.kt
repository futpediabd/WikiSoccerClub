package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.media.*
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun PressConferenceScreen(
    questions: List<PressQuestion>,
    onAnswer: (
        PressQuestion,
        PressAnswerTone
    ) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Coletiva de imprensa",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (questions.isEmpty()) {
                Text(
                    text = "Nenhuma pergunta pendente.",
                    modifier = Modifier.padding(16.dp),
                    color = WscBlueDark
                )
            } else {
                LazyColumn {
                    items(
                        questions,
                        key = { it.id }
                    ) { question ->
                        var selectedTone by remember(
                            question.id
                        ) {
                            mutableStateOf(
                                PressAnswerTone.CALM
                            )
                        }

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = question.question,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))

                            PressAnswerTone.entries
                                .forEach { tone ->
                                    Button(
                                        onClick = {
                                            selectedTone = tone
                                            onAnswer(
                                                question,
                                                selectedTone
                                            )
                                        },
                                        modifier =
                                            Modifier.fillMaxWidth()
                                    ) {
                                        Text(tone.name)
                                    }
                                    Spacer(
                                        Modifier.height(5.dp)
                                    )
                                }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
