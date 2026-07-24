package com.wikisoccerclub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.ui.theme.WscBlue
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreen
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscOutline
import com.wikisoccerclub.ui.theme.WscWhite
import com.wikisoccerclub.ui.theme.WscYellow

val WscPageGradient = Brush.verticalGradient(
    colors = listOf(WscGreenDark, WscGreen, WscGreen)
)

val WscHeaderGradient = Brush.horizontalGradient(
    colors = listOf(WscGreenDark, WscBlueDark)
)

@Composable
fun WscScreenBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WscPageGradient)
    ) {
        content()
    }
}

@Composable
fun WscHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))
            .background(WscHeaderGradient)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = WscWhite
                    )
                }
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = WscWhite,
                    fontWeight = FontWeight.Bold
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WscWhite.copy(alpha = 0.86f)
                    )
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(WscYellow)
        )
    }
}

@Composable
fun WscTopBar(title: String, onBack: (() -> Unit)? = null) {
    WscHeader(title = title, onBack = onBack)
}

@Composable
fun WscWhiteCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.border(1.dp, WscOutline, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = WscWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}

@Composable
fun WscPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = WscYellow,
            contentColor = WscBlueDark,
            disabledContainerColor = WscYellow.copy(alpha = 0.45f),
            disabledContentColor = WscBlue.copy(alpha = 0.55f)
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}
