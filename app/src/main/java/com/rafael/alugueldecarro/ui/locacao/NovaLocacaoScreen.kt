package com.rafael.alugueldecarro.ui.locacao

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rafael.alugueldecarro.domain.model.Veiculo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaLocacaoScreen(
    viewModel: LocacaoViewModel,
    onSelecionarCliente: () -> Unit,
    onLocacaoConcluida: () -> Unit
) {

    val veiculosDisponiveis by
    viewModel.veiculosDisponiveis
        .collectAsStateWithLifecycle()

    val clienteSelecionado by
    viewModel.clienteSelecionado
        .collectAsStateWithLifecycle()

    val mensagem by
    viewModel.mensagem
        .collectAsStateWithLifecycle()

    /*
     * Agora observamos a confirmação real
     * da gravação.
     */
    val locacaoConcluida by
    viewModel.locacaoConcluida
        .collectAsStateWithLifecycle()

    var veiculoSelecionado by remember {
        mutableStateOf<Veiculo?>(null)
    }

    var menuVeiculosAberto by remember {
        mutableStateOf(false)
    }

    var dataSaida by remember {
        mutableStateOf<Long?>(null)
    }

    var dataEntrega by remember {
        mutableStateOf<Long?>(null)
    }

    var mostrarDatePickerSaida by remember {
        mutableStateOf(false)
    }

    var mostrarDatePickerEntrega by remember {
        mutableStateOf(false)
    }

    val valorTotal =
        viewModel.calcularValorTotal(
            veiculo = veiculoSelecionado,
            dataSaida = dataSaida,
            dataEntrega = dataEntrega
        )

    /*
     * A navegação só acontece quando o ViewModel
     * informar que a locação foi realmente
     * concluída.
     */
    LaunchedEffect(
        locacaoConcluida
    ) {

        if (locacaoConcluida) {

            viewModel
                .consumirLocacaoConcluida()

            onLocacaoConcluida()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Nova Locação",
                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Text(
                            text =
                                "Preencha os dados da locação",
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    14.dp
                )
        ) {

            // =================================================
            // VEÍCULO
            // =================================================

            ExposedDropdownMenuBox(
                expanded =
                    menuVeiculosAberto,

                onExpandedChange = {

                    menuVeiculosAberto =
                        !menuVeiculosAberto
                }
            ) {

                OutlinedTextField(
                    value =
                        veiculoSelecionado
                            ?.let {

                                "${it.marca} ${it.modelo} - ${it.placa}"

                            } ?: "",

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Veículo")
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default
                                    .DirectionsCar,

                            contentDescription =
                                null
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    menuVeiculosAberto
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded =
                        menuVeiculosAberto,

                    onDismissRequest = {

                        menuVeiculosAberto =
                            false
                    }
                ) {

                    veiculosDisponiveis
                        .forEach { veiculo ->

                            DropdownMenuItem(
                                text = {

                                    Column {

                                        Text(
                                            text =
                                                "${veiculo.marca} ${veiculo.modelo}"
                                        )

                                        Text(
                                            text =
                                                "${veiculo.placa} • R$ %.2f/dia"
                                                    .format(
                                                        veiculo.valorDiaria
                                                    ),

                                            style =
                                                MaterialTheme
                                                    .typography
                                                    .bodySmall
                                        )
                                    }
                                },

                                onClick = {

                                    veiculoSelecionado =
                                        veiculo

                                    menuVeiculosAberto =
                                        false
                                }
                            )
                        }
                }
            }

            // =================================================
            // CLIENTE
            // =================================================

            OutlinedTextField(
                value =
                    clienteSelecionado
                        ?.let {

                            "${it.nome} - ${it.telefone}"

                        } ?: "",

                onValueChange = {},

                readOnly = true,

                label = {
                    Text("Cliente")
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Person,

                        contentDescription =
                            null
                    )
                },

                placeholder = {

                    Text(
                        "Nenhum cliente selecionado"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth()
            )

            FilledTonalButton(
                onClick =
                    onSelecionarCliente,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Person,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    "Selecionar Cliente"
                )
            }

            // =================================================
            // DATAS
            // =================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                FilledTonalButton(
                    onClick = {

                        mostrarDatePickerSaida =
                            true
                    },

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Icon(
                        imageVector =
                            Icons.Default
                                .CalendarMonth,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        text =
                            if (
                                dataSaida ==
                                null
                            ) {

                                "Saída"

                            } else {

                                formatarData(
                                    dataSaida!!
                                )
                            }
                    )
                }

                FilledTonalButton(
                    onClick = {

                        mostrarDatePickerEntrega =
                            true
                    },

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Icon(
                        imageVector =
                            Icons.Default
                                .CalendarMonth,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.width(6.dp)
                    )

                    Text(
                        text =
                            if (
                                dataEntrega ==
                                null
                            ) {

                                "Entrega"

                            } else {

                                formatarData(
                                    dataEntrega!!
                                )
                            }
                    )
                }
            }

            // =================================================
            // VALOR ESTIMADO
            // =================================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .surfaceVariant
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(
                            16.dp
                        )
                ) {

                    Text(
                        text =
                            "Valor estimado",

                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )

                    Text(
                        text =
                            "R$ %.2f"
                                .format(
                                    valorTotal
                                ),

                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )
                }
            }

            // =================================================
            // MENSAGEM
            // =================================================

            mensagem?.let {

                Text(
                    text = it,

                    color =
                        if (
                            it.contains(
                                "sucesso",
                                ignoreCase = true
                            )
                        ) {

                            MaterialTheme
                                .colorScheme
                                .primary

                        } else {

                            MaterialTheme
                                .colorScheme
                                .error
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )

            // =================================================
            // CONFIRMAR
            // =================================================

            Button(
                onClick = {

                    /*
                     * Agora apenas pedimos ao
                     * ViewModel para confirmar.
                     *
                     * NÃO navegamos daqui.
                     */
                    viewModel
                        .confirmarLocacao(
                            veiculo =
                                veiculoSelecionado,

                            dataSaida =
                                dataSaida,

                            dataEntrega =
                                dataEntrega
                        )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Save,

                    contentDescription =
                        null
                )

                Spacer(
                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )

                Text(
                    "Confirmar Locação"
                )
            }
        }
    }

    // =========================================================
    // DATE PICKER - SAÍDA
    // =========================================================

    if (
        mostrarDatePickerSaida
    ) {

        val datePickerState =
            rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {

                mostrarDatePickerSaida =
                    false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        dataSaida =
                            datePickerState
                                .selectedDateMillis

                        mostrarDatePickerSaida =
                            false
                    }
                ) {

                    Text("Confirmar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        mostrarDatePickerSaida =
                            false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state =
                    datePickerState
            )
        }
    }

    // =========================================================
    // DATE PICKER - ENTREGA
    // =========================================================

    if (
        mostrarDatePickerEntrega
    ) {

        val datePickerState =
            rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {

                mostrarDatePickerEntrega =
                    false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        dataEntrega =
                            datePickerState
                                .selectedDateMillis

                        mostrarDatePickerEntrega =
                            false
                    }
                ) {

                    Text("Confirmar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        mostrarDatePickerEntrega =
                            false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state =
                    datePickerState
            )
        }
    }
}

private fun formatarData(
    dataMillis: Long
): String {

    val data =
        Instant
            .ofEpochMilli(
                dataMillis
            )
            .atZone(
                ZoneId.systemDefault()
            )
            .toLocalDate()

    return data.format(
        DateTimeFormatter
            .ofPattern(
                "dd/MM/yyyy"
            )
    )
}