package com.rafael.alugueldecarro.ui.contato

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContatoScreen(
    viewModel: ContatoViewModel,
    onContatoSelecionado: (Contato) -> Unit
) {

    val context = LocalContext.current
    val activity = context as? Activity

    val contatos by viewModel.contatos.collectAsStateWithLifecycle()
    val busca by viewModel.busca.collectAsStateWithLifecycle()
    val carregando by viewModel.carregando.collectAsStateWithLifecycle()
    val erro by viewModel.erro.collectAsStateWithLifecycle()

    var permissaoConcedida by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissaoJaSolicitada by rememberSaveable {
        mutableStateOf(false)
    }

    val launcherPermissao =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { concedida ->

            permissaoJaSolicitada = true
            permissaoConcedida = concedida

            if (concedida) {
                viewModel.carregarContatos()
            }
        }

    LaunchedEffect(Unit) {

        if (permissaoConcedida) {

            viewModel.carregarContatos()

        } else if (!permissaoJaSolicitada) {

            permissaoJaSolicitada = true

            launcherPermissao.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {

                        Text(
                            text = "Selecionar Cliente",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Escolha um contato da agenda",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (!permissaoConcedida) {

            val podeSolicitarNovamente =
                activity != null &&
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.READ_CONTACTS
                        )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.ContactPhone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Acesso aos contatos",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        if (podeSolicitarNovamente) {
                            "Precisamos da permissão para buscar clientes na agenda do aparelho."
                        } else {
                            "O acesso aos contatos está bloqueado. Abra as configurações do aplicativo para permitir."
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Button(
                    onClick = {

                        if (podeSolicitarNovamente) {

                            launcherPermissao.launch(
                                Manifest.permission.READ_CONTACTS
                            )

                        } else {

                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            ).apply {

                                data = Uri.fromParts(
                                    "package",
                                    context.packageName,
                                    null
                                )
                            }

                            context.startActivity(intent)
                        }
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text =
                            if (podeSolicitarNovamente) {
                                "Tentar novamente"
                            } else {
                                "Abrir configurações"
                            }
                    )
                }
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {

                OutlinedTextField(
                    value = busca,

                    onValueChange = {
                        viewModel.atualizarBusca(it)
                    },

                    label = {
                        Text("Buscar contato")
                    },

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },

                    singleLine = true,

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

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
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    contatos.isEmpty() -> {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                text = "Nenhum contato encontrado",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Tente buscar por outro nome.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),

                            contentPadding = PaddingValues(
                                bottom = 16.dp
                            ),

                            verticalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            items(contatos) { contato ->

                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onContatoSelecionado(
                                                contato
                                            )
                                        },

                                    colors =
                                        CardDefaults.elevatedCardColors(
                                            containerColor =
                                                MaterialTheme.colorScheme.surface
                                        )
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Default.Person,
                                            contentDescription = null,
                                            tint =
                                                MaterialTheme.colorScheme.primary
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.width(12.dp)
                                        )

                                        Column(
                                            modifier =
                                                Modifier.weight(1f)
                                        ) {

                                            Text(
                                                text = contato.nome,
                                                style =
                                                    MaterialTheme.typography.titleMedium
                                            )

                                            Spacer(
                                                modifier =
                                                    Modifier.height(4.dp)
                                            )

                                            Row(
                                                verticalAlignment =
                                                    Alignment.CenterVertically
                                            ) {

                                                Icon(
                                                    imageVector =
                                                        Icons.Default.Phone,
                                                    contentDescription = null,
                                                    tint =
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                Spacer(
                                                    modifier =
                                                        Modifier.width(6.dp)
                                                )

                                                Text(
                                                    text =
                                                        contato.telefone,
                                                    style =
                                                        MaterialTheme.typography.bodyMedium,
                                                    color =
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}