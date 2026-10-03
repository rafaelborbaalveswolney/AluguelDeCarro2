package com.rafael.alugueldecarro.ui.locacao

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.rafael.alugueldecarro.domain.model.Locacao
import com.rafael.alugueldecarro.domain.model.LocacaoDetalhada
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoLocacoesScreen(
    viewModel: HistoricoViewModel,
    onSincronizarLocacao: (Locacao) -> Unit,
    mensagemSincronizacao: String? = null
) {

    val locacoes by viewModel.locacoes.collectAsStateWithLifecycle()
    val carregando by viewModel.carregando.collectAsStateWithLifecycle()
    val erro by viewModel.erro.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Histórico de Locações",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Consulte locações ativas e finalizadas",
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
        ) {

            mensagemSincronizacao?.let {

                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    color = Color(0xFF43A047)
                )
            }

            when {

                carregando -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
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
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Text(
                            text = "Nenhuma locação encontrada",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "As locações aparecerão aqui.",
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
                            bottom = 24.dp
                        ),

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(locacoes) { item ->

                            CardHistorico(
                                item = item,

                                onSincronizar = {
                                    onSincronizarLocacao(
                                        item.locacao
                                    )
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
private fun CardHistorico(
    item: LocacaoDetalhada,
    onSincronizar: () -> Unit
) {

    val statusAtivo =
        item.locacao.status == "ATIVA"

    val corStatus =
        if (statusAtivo) {
            Color(0xFF1565C0)
        } else {
            Color(0xFF43A047)
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

                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = corStatus
                        )

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        Text(
                            text =
                                if (statusAtivo) {
                                    "Ativa"
                                } else {
                                    "Finalizada"
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
                    text =
                        "${formatarDataHistorico(item.locacao.dataSaida)} até " +
                                formatarDataHistorico(
                                    item.locacao.dataEntregaPrevista
                                )
                )
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Valor da locação",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "R$ %.2f".format(
                            item.locacao.valorTotal
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedButton(
                    onClick = onSincronizar
                ) {

                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text("Sincronizar")
                }
            }
        }
    }
}

private fun formatarDataHistorico(
    dataMillis: Long
): String {

    val data = Instant
        .ofEpochMilli(dataMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return data.format(
        DateTimeFormatter.ofPattern(
            "dd/MM/yyyy"
        )
    )
}