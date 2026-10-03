package com.rafael.alugueldecarro.ui.veiculo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroVeiculoScreen(
    viewModel: VeiculoViewModel,
    onVoltar: () -> Unit
) {

    var marca by remember {
        mutableStateOf("")
    }

    var modelo by remember {
        mutableStateOf("")
    }

    var placa by remember {
        mutableStateOf("")
    }

    var ano by remember {
        mutableStateOf("")
    }

    var valorDiaria by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Novo Veículo",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Preencha os dados do veículo",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OutlinedTextField(
                value = marca,

                onValueChange = {
                    marca = it
                },

                label = {
                    Text("Marca")
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalOffer,
                        contentDescription = null
                    )
                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = modelo,

                onValueChange = {
                    modelo = it
                },

                label = {
                    Text("Modelo")
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null
                    )
                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = placa,

                onValueChange = {
                    placa = it.uppercase()
                },

                label = {
                    Text("Placa")
                },

                placeholder = {
                    Text("AAA-1234 ou AAA1A23")
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null
                    )
                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ano,

                onValueChange = {
                    ano = it
                },

                label = {
                    Text("Ano")
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null
                    )
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valorDiaria,

                onValueChange = {
                    valorDiaria = it
                },

                label = {
                    Text("Valor da diária")
                },

                placeholder = {
                    Text("Ex.: 150,00")
                },

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null
                    )
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Button(
                onClick = {

                    val anoNumero =
                        ano.toIntOrNull()

                    val valorNumero =
                        valorDiaria
                            .replace(",", ".")
                            .toDoubleOrNull()

                    val placaValida =
                        Regex(
                            "^[A-Z]{3}-?[0-9]{4}$"
                        ).matches(placa) ||
                                Regex(
                                    "^[A-Z]{3}[0-9][A-Z][0-9]{2}$"
                                ).matches(placa)

                    when {

                        marca.isBlank() ||
                                modelo.isBlank() ||
                                placa.isBlank() ||
                                ano.isBlank() ||
                                valorDiaria.isBlank() -> {

                            erro =
                                "Preencha todos os campos."
                        }

                        !placaValida -> {

                            erro =
                                "Placa inválida. Use AAA-1234 ou AAA1A23."
                        }

                        anoNumero == null -> {

                            erro =
                                "Informe um ano válido."
                        }

                        valorNumero == null ||
                                valorNumero <= 0 -> {

                            erro =
                                "O valor da diária deve ser maior que zero."
                        }

                        else -> {

                            erro = null

                            viewModel.cadastrarVeiculo(
                                marca = marca.trim(),
                                modelo = modelo.trim(),
                                placa = placa.trim(),
                                ano = anoNumero,
                                valorDiaria = valorNumero
                            )

                            onVoltar()
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )

                Text(
                    text = "  Cadastrar Veículo"
                )
            }
        }
    }
}