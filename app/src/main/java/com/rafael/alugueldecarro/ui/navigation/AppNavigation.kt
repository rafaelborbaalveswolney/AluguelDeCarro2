package com.rafael.alugueldecarro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rafael.alugueldecarro.data.local.DatabaseProvider
import com.rafael.alugueldecarro.data.remote.RetrofitClient
import com.rafael.alugueldecarro.data.remote.SyncRemoteDataSource
import com.rafael.alugueldecarro.data.repository.ClienteRepositoryImpl
import com.rafael.alugueldecarro.data.repository.LocacaoRepositoryImpl
import com.rafael.alugueldecarro.data.repository.SyncRepositoryImpl
import com.rafael.alugueldecarro.data.repository.VeiculoRepositoryImpl
import com.rafael.alugueldecarro.ui.contato.ContatoRepository
import com.rafael.alugueldecarro.ui.contato.ContatoScreen
import com.rafael.alugueldecarro.ui.contato.ContatoViewModel
import com.rafael.alugueldecarro.ui.contato.ContatoViewModelFactory
import com.rafael.alugueldecarro.ui.dashboard.DashboardScreen
import com.rafael.alugueldecarro.ui.dashboard.DashboardViewModel
import com.rafael.alugueldecarro.ui.dashboard.DashboardViewModelFactory
import com.rafael.alugueldecarro.ui.locacao.HistoricoLocacoesScreen
import com.rafael.alugueldecarro.ui.locacao.HistoricoViewModel
import com.rafael.alugueldecarro.ui.locacao.HistoricoViewModelFactory
import com.rafael.alugueldecarro.ui.locacao.LocacaoViewModel
import com.rafael.alugueldecarro.ui.locacao.LocacaoViewModelFactory
import com.rafael.alugueldecarro.ui.locacao.NovaLocacaoScreen
import com.rafael.alugueldecarro.ui.sync.SyncViewModel
import com.rafael.alugueldecarro.ui.sync.SyncViewModelFactory
import com.rafael.alugueldecarro.ui.veiculo.CadastroVeiculoScreen
import com.rafael.alugueldecarro.ui.veiculo.ListaVeiculosScreen
import com.rafael.alugueldecarro.ui.veiculo.VeiculoViewModel
import com.rafael.alugueldecarro.ui.veiculo.VeiculoViewModelFactory

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val context =
        LocalContext.current

    val database =
        DatabaseProvider.getDatabase(context)

    val veiculoRepository =
        VeiculoRepositoryImpl(
            database.veiculoDao()
        )

    val clienteRepository =
        ClienteRepositoryImpl(
            database.clienteDao()
        )

    val locacaoRepository =
        LocacaoRepositoryImpl(
            database.locacaoDao()
        )

    val syncRemoteDataSource =
        SyncRemoteDataSource(
            RetrofitClient.apiService
        )

    val syncRepository =
        SyncRepositoryImpl(
            syncRemoteDataSource
        )

    val veiculoViewModelFactory =
        VeiculoViewModelFactory(
            veiculoRepository
        )

    val dashboardViewModelFactory =
        DashboardViewModelFactory(
            locacaoRepository,
            veiculoRepository
        )

    val locacaoViewModelFactory =
        LocacaoViewModelFactory(
            veiculoRepository,
            clienteRepository,
            locacaoRepository
        )

    val historicoViewModelFactory =
        HistoricoViewModelFactory(
            locacaoRepository
        )

    val syncViewModelFactory =
        SyncViewModelFactory(
            syncRepository
        )

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        // =====================================================
        // DASHBOARD - TELA INICIAL
        // =====================================================

        composable(
            route = Routes.DASHBOARD
        ) {

            val viewModel: DashboardViewModel =
                viewModel(
                    factory = dashboardViewModelFactory
                )

            DashboardScreen(
                viewModel = viewModel,

                onNovaLocacao = {
                    navController.navigate(
                        Routes.NOVA_LOCACAO
                    )
                },

                onAbrirVeiculos = {
                    navController.navigate(
                        Routes.LISTA_VEICULOS
                    )
                },

                onAbrirHistorico = {
                    navController.navigate(
                        Routes.HISTORICO_LOCACOES
                    )
                }
            )
        }

        // =====================================================
        // LISTA DE VEÍCULOS
        // =====================================================

        composable(
            route = Routes.LISTA_VEICULOS
        ) {

            val veiculoViewModel: VeiculoViewModel =
                viewModel(
                    factory = veiculoViewModelFactory
                )

            val syncViewModel: SyncViewModel =
                viewModel(
                    factory = syncViewModelFactory
                )

            ListaVeiculosScreen(
                viewModel = veiculoViewModel,

                mensagemSincronizacao =
                    syncViewModel.mensagem.value,

                onNovoVeiculo = {
                    navController.navigate(
                        Routes.CADASTRO_VEICULO
                    )
                },

                onSincronizarVeiculo = { veiculo ->
                    syncViewModel.sincronizarVeiculo(
                        veiculo
                    )
                }
            )
        }

        // =====================================================
        // CADASTRO DE VEÍCULO
        // =====================================================

        composable(
            route = Routes.CADASTRO_VEICULO
        ) {

            val viewModel: VeiculoViewModel =
                viewModel(
                    factory = veiculoViewModelFactory
                )

            CadastroVeiculoScreen(
                viewModel = viewModel,

                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // NOVA LOCAÇÃO
        // =====================================================

        composable(
            route = Routes.NOVA_LOCACAO
        ) { backStackEntry ->

            val viewModel: LocacaoViewModel =
                viewModel(
                    factory = locacaoViewModelFactory
                )

            val contatoNome =
                backStackEntry
                    .savedStateHandle
                    .get<String>(
                        "contato_nome"
                    )

            val contatoTelefone =
                backStackEntry
                    .savedStateHandle
                    .get<String>(
                        "contato_telefone"
                    )

            val contatoId =
                backStackEntry
                    .savedStateHandle
                    .get<Long>(
                        "contato_id"
                    )

            LaunchedEffect(
                contatoNome,
                contatoTelefone,
                contatoId
            ) {

                if (
                    contatoNome != null &&
                    contatoTelefone != null &&
                    contatoId != null
                ) {

                    viewModel.selecionarCliente(
                        nome = contatoNome,
                        telefone = contatoTelefone,
                        contatoId = contatoId
                    )

                    backStackEntry
                        .savedStateHandle
                        .remove<String>(
                            "contato_nome"
                        )

                    backStackEntry
                        .savedStateHandle
                        .remove<String>(
                            "contato_telefone"
                        )

                    backStackEntry
                        .savedStateHandle
                        .remove<Long>(
                            "contato_id"
                        )
                }
            }

            NovaLocacaoScreen(
                viewModel = viewModel,

                onSelecionarCliente = {
                    navController.navigate(
                        Routes.CONTATOS
                    )
                },

                onLocacaoConcluida = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // CONTATOS
        // =====================================================

        composable(
            route = Routes.CONTATOS
        ) {

            val contatoRepository =
                ContatoRepository(
                    context.contentResolver
                )

            val contatoViewModelFactory =
                ContatoViewModelFactory(
                    contatoRepository
                )

            val viewModel: ContatoViewModel =
                viewModel(
                    factory = contatoViewModelFactory
                )

            ContatoScreen(
                viewModel = viewModel,

                onContatoSelecionado = { contato ->

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contato_nome",
                            contato.nome
                        )

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contato_telefone",
                            contato.telefone
                        )

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contato_id",
                            contato.id
                        )

                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // HISTÓRICO
        // =====================================================

        composable(
            route = Routes.HISTORICO_LOCACOES
        ) {

            val historicoViewModel: HistoricoViewModel =
                viewModel(
                    factory = historicoViewModelFactory
                )

            val syncViewModel: SyncViewModel =
                viewModel(
                    factory = syncViewModelFactory
                )

            HistoricoLocacoesScreen(
                viewModel = historicoViewModel,

                mensagemSincronizacao =
                    syncViewModel.mensagem.value,

                onSincronizarLocacao = { locacao ->
                    syncViewModel.sincronizarLocacao(
                        locacao
                    )
                }
            )
        }
    }
}