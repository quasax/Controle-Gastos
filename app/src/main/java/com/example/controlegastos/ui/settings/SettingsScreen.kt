package com.example.controlegastos.ui.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults

@Composable
private fun TopBarConfiguracoes(
    onVoltar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFECF0ED))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botão voltar com fundo branco e borda clara
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                tonalElevation = 0.dp,
                border = BorderStroke(1.dp, Color(0xFFE6EFEA)),
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onVoltar() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF2F6F62),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Título + subtítulo
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Configurações",
                    color = Color(0xFF123C3A),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ajuste suas preferências",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SettingsPerfilPanel(
    nomeAtual: String,
    onNomeAlterado: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "PERFIL",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF8A929B),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Como você gostaria de ser chamado",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB0B4BA),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nomeAtual,
                    onValueChange = onNomeAlterado,
                    label = { Text("Seu Nome") },
                    placeholder = { Text("Ex: Cesar") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ícone de Perfil",
                            tint = Color(0xFF1B6B4A)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B6B4A),
                        unfocusedBorderColor = Color(0xFFE6EFEA),
                        focusedLabelColor = Color(0xFF1B6B4A),
                        cursorColor = Color(0xFF1B6B4A)
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onVoltar: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val exportarArquivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let(viewModel::exportar)
    }

    val abrirArquivo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { arquivoSelecionado ->
            viewModel.restaurar(arquivoSelecionado)
        }
    }

    var mostrarConfirmacaoRestauracao by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        uiState.mensagemSucesso,
        uiState.mensagemErro
    ) {
        val mensagem = uiState.mensagemSucesso ?: uiState.mensagemErro

        mensagem?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagens()
        }
    }

    if (mostrarConfirmacaoRestauracao) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacaoRestauracao = false
            },
            title = {
                Text(text = "Restaurar backup?")
            },
            text = {
                Text(
                    text = "Os dados atuais serão apagados e substituídos " +
                            "pelos dados do arquivo selecionado. Esta ação não pode ser desfeita."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacaoRestauracao = false
                        abrirArquivo.launch(arrayOf("application/json"))
                    }
                ) {
                    Text(text = "Selecionar arquivo")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarConfirmacaoRestauracao = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    // Apenas UM Scaffold limpo e estruturado contendo ambos os painéis
    Scaffold(
        topBar = {
            TopBarConfiguracoes(onVoltar = onVoltar)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFECF0ED))
                .padding(innerPadding)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Painel para digitar o nome
                SettingsPerfilPanel(
                    nomeAtual = uiState.nomeUsuario,
                    onNomeAlterado = { novoNome ->
                        viewModel.atualizarNome(novoNome)
                    }
                )

                // 2. Painel de Backup existente
                SettingsBackupPanel(
                    ultimoBackupEpochMillis = uiState.ultimoBackupEpochMillis,
                    onExportar = { exportarArquivo.launch("meu_controle_financeiro_backup.json") },
                    onRestaurar = { mostrarConfirmacaoRestauracao = true },
                    isLoading = uiState.isLoading
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun SettingsBackupPanel(
    ultimoBackupEpochMillis: Long?,
    onExportar: () -> Unit,
    onRestaurar: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "BACKUP MANUAL",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF8A929B),
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = formatarUltimoBackup(ultimoBackupEpochMillis),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB0B4BA),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Opção 1: Exportar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isLoading, onClick = onExportar)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val corExportar = Color(0xFF1B6B4A)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(corExportar.copy(alpha = 0.12f))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Exportar",
                            tint = corExportar,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Exportar dados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF143045),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Salvar um arquivo no dispositivo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9CA0A9)
                        )
                    }
                }

                HorizontalDivider(
                    color = Color(0xFFF0F2F4),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                // Opção 2: Restaurar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isLoading, onClick = onRestaurar)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val corRestaurar = Color(0xFFD84315)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(corRestaurar.copy(alpha = 0.12f))
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Restore,
                            contentDescription = "Restaurar",
                            tint = corRestaurar,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Restaurar backup",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF143045),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Substituir os dados atuais",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9CA0A9)
                        )
                    }
                }
            }
        }
    }
}

private fun formatarUltimoBackup(
    epochMillis: Long?
): String {
    if (epochMillis == null) {
        return "Nenhum backup realizado neste dispositivo."
    }

    val dataHora = Instant
        .ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern(
                "dd/MM/yyyy 'às' HH:mm"
            )
        )

    return "Último backup: $dataHora"
}