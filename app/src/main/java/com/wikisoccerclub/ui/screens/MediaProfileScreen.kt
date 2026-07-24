package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.data.media.MediaProfile
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun MediaProfileScreen(
    profile: MediaProfile?,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Relação com a imprensa",
            onBack = onBack
        )

        WscWhiteCard(
            Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                if (profile == null) {
                    Text(
                        text = "Perfil de imprensa indisponível.",
                        color = WscBlueDark
                    )
                } else {
                    Text(
                        text = "Relacionamento com a mídia",
                        color = WscBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${profile.mediaRelationship}/100",
                        color = WscGreenDark
                    )
                    LinearProgressIndicator(
                        progress = {
                            profile.mediaRelationship / 100f
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Reputação na imprensa: " +
                            profile.pressReputation,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Entrevistas: " +
                            profile.interviewsGiven,
                        color = WscGreenDark
                    )
                    Text(
                        text = "Respostas polêmicas: " +
                            profile.controversialAnswers,
                        color = WscBlueDark
                    )
                }
            }
        }
    }
}
