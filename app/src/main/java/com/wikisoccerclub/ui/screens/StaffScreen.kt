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
import com.wikisoccerclub.data.training.StaffMember
import com.wikisoccerclub.ui.components.WscTopBar
import com.wikisoccerclub.ui.components.WscWhiteCard
import com.wikisoccerclub.ui.theme.*

@Composable
fun StaffScreen(
    staff: List<StaffMember>,
    monthlyCost: Long,
    onDismiss: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(WscGreen)
            .padding(16.dp)
    ) {
        WscTopBar(
            title = "Comissão técnica",
            onBack = onBack
        )

        WscWhiteCard(Modifier.fillMaxWidth()) {
            Text(
                text =
                    "Custo mensal: $monthlyCost",
                modifier = Modifier.padding(14.dp),
                color = WscBlueDark,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        WscWhiteCard(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn {
                items(
                    staff,
                    key = { it.id }
                ) { member ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = member.name,
                            color = WscBlueDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = member.role.name,
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Treino: ${member.coaching} • " +
                                    "Tática: ${member.tacticalKnowledge}",
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Salário: ${member.monthlySalary}",
                            color = WscGreenDark
                        )
                        Text(
                            text =
                                "Contrato até ${member.contractEndYear}",
                            color = WscBlueDark
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                onDismiss(member.id)
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Demitir")
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
