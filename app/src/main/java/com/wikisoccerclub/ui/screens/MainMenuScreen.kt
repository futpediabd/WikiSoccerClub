package com.wikisoccerclub.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wikisoccerclub.R
import com.wikisoccerclub.ui.components.WscHeaderGradient
import com.wikisoccerclub.ui.components.WscScreenBackground
import com.wikisoccerclub.ui.theme.WscBlueDark
import com.wikisoccerclub.ui.theme.WscGreenDark
import com.wikisoccerclub.ui.theme.WscTextMuted
import com.wikisoccerclub.ui.theme.WscWhite
import com.wikisoccerclub.ui.theme.WscYellow

private data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val action: () -> Unit
)

@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit,
    onLoadGame: () -> Unit,
    onEditor: () -> Unit,
    onCredits: () -> Unit
) {
    val items = listOf(
        MenuItem("Novo Jogo", "Iniciar uma nova carreira", Icons.Default.PlayArrow, onNewGame),
        MenuItem("Carregar Jogo", "Continuar uma carreira salva", Icons.Default.FolderOpen, onLoadGame),
        MenuItem("Editor", "Visualizar dados importados", Icons.Default.Edit, onEditor),
        MenuItem("Créditos", "Informações da versão", Icons.Default.Info, onCredits)
    )

    WscScreenBackground {
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(WscHeaderGradient)
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.wsc_logo_oficial),
                    contentDescription = "Logo oficial do WikiSoccerClub",
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    "WikiSoccerClub",
                    style = MaterialTheme.typography.headlineMedium,
                    color = WscWhite,
                    fontWeight = FontWeight.Bold
                )
                Text("Gestão, história e futebol", color = WscWhite.copy(alpha = 0.86f))
                Spacer(Modifier.height(12.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(WscYellow, RoundedCornerShape(6.dp))
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    Card(
                        onClick = item.action,
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = WscWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.height(154.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(WscYellow, RoundedCornerShape(17.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    item.title,
                                    tint = WscGreenDark,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                item.title,
                                color = WscBlueDark,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                item.subtitle,
                                color = WscTextMuted,
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
