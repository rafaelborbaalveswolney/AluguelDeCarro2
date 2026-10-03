package com.rafael.alugueldecarro.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNovaLocacao: () -> Unit,
    onLocacoesAtivas: () -> Unit,
    onVeiculos: () -> Unit,
    onHistorico: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Aluguel de Carros",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Gestão da sua locadora",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.elevatedCardColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer
                ),

                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(18.dp)
                    )

                    Column {

                        Text(
                            text = "Controle sua locadora",
                            style =
                                MaterialTheme.typography.titleLarge,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Veículos, locações e histórico em um só lugar.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Acesso rápido",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Escolha uma opção para continuar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            MenuHomeItem(
                titulo = "Nova Locação",
                descricao = "Registrar uma nova locação",
                icone = Icons.Default.AddCircle,
                onClick = onNovaLocacao
            )

            MenuHomeItem(
                titulo = "Locações Ativas",
                descricao = "Acompanhar as locações em andamento",
                icone = Icons.Default.ListAlt,
                onClick = onLocacoesAtivas
            )

            MenuHomeItem(
                titulo = "Veículos",
                descricao = "Cadastrar e gerenciar sua frota",
                icone = Icons.Default.DirectionsCar,
                onClick = onVeiculos
            )

            MenuHomeItem(
                titulo = "Histórico",
                descricao = "Consultar locações anteriores",
                icone = Icons.Default.History,
                onClick = onHistorico
            )
        }
    }
}

@Composable
private fun MenuHomeItem(
    titulo: String,
    descricao: String,
    icone: ImageVector,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.elevatedCardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color =
                            MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(14.dp)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    tint =
                        MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}