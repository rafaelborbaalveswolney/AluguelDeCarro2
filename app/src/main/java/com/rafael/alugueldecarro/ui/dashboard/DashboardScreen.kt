package com.rafael.alugueldecarro.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNovaLocacao: () -> Unit,
    onAbrirVeiculos: () -> Unit,
    onAbrirHistorico: () -> Unit
) {

    val locacoes by viewModel.locacoes.collectAsStateWithLifecycle()
    val carregando by viewModel.carregando.collectAsStateWithLifecycle()
    val erro by viewModel.erro.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Gestão de Locações",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Acompanhe suas locações ativas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },

        bottomBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilledTonalButton(
                    onClick = onAbrirVeiculos,
                    modifier = Modifier.weight(1f)
                ) {

                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Veículos")
                }

                FilledTonalButton(
                    onClick = onAbrirHistorico,
                    modifier = Modifier.weight(1f)
                ) {

                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Histórico")
                }
            }
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovaLocacao,

                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },

                text = {
                    Text("Nova Locação")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            mensagem?.let {

                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            when {

                carregando -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }

                erro != null -> {

                    Text(
                        text = erro ?: "Erro desconhecido",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                locacoes.isEmpty() -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Nenhuma locação ativa",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Toque em Nova Locação para começar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),

                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 100.dp
                        ),

                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(locacoes) { item ->

                            CardLocacao(
                                item = item,

                                onFinalizar = {
                                    viewModel.finalizarLocacao(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardLocacao(
    item: LocacaoDetalhada,
    onFinalizar: () -> Unit
) {

    val dataSaida =
        converterData(item.locacao.dataSaida)

    val dataEntrega =
        converterData(item.locacao.dataEntregaPrevista)

    val diasFaltantes =
        calcularDiasFaltantes(
            item.locacao.dataEntregaPrevista
        )

    val atrasada =
        diasFaltantes < 0

    val corStatus =
        if (atrasada) {
            Color(0xFFE53935)
        } else {
            Color(0xFF1565C0)
        }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.elevatedCardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "${item.veiculo.marca} ${item.veiculo.modelo}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = item.veiculo.placa,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = corStatus.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (atrasada) {

                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = corStatus
                            )

                            Spacer(
                                modifier = Modifier.width(5.dp)
                            )
                        }

                        Text(
                            text =
                                if (atrasada) {
                                    "Atrasada"
                                } else {
                                    "Ativa"
                                },
                            color = corStatus,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = item.cliente.nome
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = item.cliente.telefone
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "$dataSaida até $dataEntrega"
                )
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text =
                    if (atrasada) {
                        "Atrasada há ${-diasFaltantes} dia(s)"
                    } else {
                        "Faltam $diasFaltantes dia(s)"
                    },
                color = corStatus,
                style = MaterialTheme.typography.titleSmall
            )

            Button(
                onClick = onFinalizar,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Finalizar Locação")
            }
        }
    }
}

private fun converterData(
    dataMillis: Long
): String {

    val data = Instant
        .ofEpochMilli(dataMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return data.format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )
}

private fun calcularDiasFaltantes(
    dataEntregaMillis: Long
): Long {

    val hoje =
        java.time.LocalDate.now()

    val dataEntrega = Instant
        .ofEpochMilli(dataEntregaMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return ChronoUnit.DAYS.between(
        hoje,
        dataEntrega
    )
}