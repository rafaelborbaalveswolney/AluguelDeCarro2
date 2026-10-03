package com.rafael.alugueldecarro.ui.veiculo

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rafael.alugueldecarro.domain.model.Veiculo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaVeiculosScreen(
    viewModel: VeiculoViewModel,
    onNovoVeiculo: () -> Unit,
    onSincronizarVeiculo: (Veiculo) -> Unit,
    mensagemSincronizacao: String? = null
) {

    val veiculos by viewModel.veiculos.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Frota de Veículos",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Gerencie os veículos cadastrados",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovoVeiculo,

                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar veículo"
                    )
                },

                text = {
                    Text("Adicionar")
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

            if (veiculos.isEmpty()) {

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
                        text = "Nenhum veículo cadastrado",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Cadastre um veículo para começar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 100.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(veiculos) { veiculo ->

                        CardVeiculo(
                            veiculo = veiculo,

                            onManutencao = {
                                viewModel.colocarEmManutencao(
                                    veiculo
                                )
                            },

                            onDisponivel = {
                                viewModel.tornarDisponivel(
                                    veiculo
                                )
                            },

                            onSincronizar = {
                                onSincronizarVeiculo(
                                    veiculo
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardVeiculo(
    veiculo: Veiculo,
    onManutencao: () -> Unit,
    onDisponivel: () -> Unit,
    onSincronizar: () -> Unit
) {

    val corStatus = when (veiculo.status) {

        "DISPONIVEL" -> Color(0xFF43A047)

        "MANUTENCAO" -> Color(0xFFF57C00)

        "ALUGADO" -> Color(0xFFE53935)

        else -> MaterialTheme.colorScheme.primary
    }

    val iconeStatus = when (veiculo.status) {

        "DISPONIVEL" ->
            Icons.Default.CheckCircle

        "MANUTENCAO" ->
            Icons.Default.Build

        else ->
            Icons.Default.Lock
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
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
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
                        text =
                            "${veiculo.marca} ${veiculo.modelo}",
                        style =
                            MaterialTheme.typography.titleMedium,
                        color =
                            MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = veiculo.placa,
                        style =
                            MaterialTheme.typography.bodyMedium,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusVeiculo(
                    status = veiculo.status,
                    cor = corStatus,
                    icone = iconeStatus
                )
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Ano",
                        style =
                            MaterialTheme.typography.labelMedium,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = veiculo.ano.toString(),
                        style =
                            MaterialTheme.typography.bodyLarge
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text = "Valor da diária",
                        style =
                            MaterialTheme.typography.labelMedium,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "R$ %.2f".format(
                            veiculo.valorDiaria
                        ),
                        style =
                            MaterialTheme.typography.bodyLarge,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            if (veiculo.status == "DISPONIVEL") {

                FilledTonalButton(
                    onClick = onManutencao,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Manutenção"
                    )
                }
            }

            if (veiculo.status == "MANUTENCAO") {

                FilledTonalButton(
                    onClick = onDisponivel,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Disponibilizar"
                    )
                }
            }

            OutlinedButton(
                onClick = onSincronizar,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.CloudSync,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Sincronizar"
                )
            }
        }
    }
}

@Composable
private fun StatusVeiculo(
    status: String,
    cor: Color,
    icone: ImageVector
) {

    Box(
        modifier = Modifier
            .background(
                color = cor.copy(
                    alpha = 0.15f
                ),
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icone,
                contentDescription = null,
                tint = cor
            )

            Spacer(
                modifier = Modifier.width(5.dp)
            )

            Text(
                text = formatarStatus(status),
                color = cor,
                style =
                    MaterialTheme.typography.labelMedium
            )
        }
    }
}

private fun formatarStatus(
    status: String
): String {

    return when (status) {

        "DISPONIVEL" ->
            "Disponível"

        "ALUGADO" ->
            "Alugado"

        "MANUTENCAO" ->
            "Manutenção"

        else ->
            status
    }
}