package com.example.giga17.presentation.ui.profil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.giga17.presentation.viewmodel.ProfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivasiKeamananScreen(
    viewModel: ProfilViewModel,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isEditingPassword by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privasi & Keamanan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Keamanan Akun",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (!isEditingPassword) {
                OutlinedButton(
                    onClick = { isEditingPassword = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Ubah Password", modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
                }
            } else {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Password Lama") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Password Baru") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Konfirmasi Password Baru") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { 
                        isEditingPassword = false
                        oldPassword = ""
                        newPassword = ""
                        confirmPassword = ""
                    }) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newPassword.isEmpty() || oldPassword.isEmpty()) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Semua kolom harus diisi.") }
                                return@Button
                            }
                            if (newPassword != confirmPassword) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Konfirmasi password tidak cocok.") }
                                return@Button
                            }
                            
                            coroutineScope.launch {
                                val result = viewModel.changePassword(oldPassword, newPassword)
                                if (result.isSuccess) {
                                    snackbarHostState.showSnackbar("Password berhasil diubah!")
                                    isEditingPassword = false
                                    oldPassword = ""
                                    newPassword = ""
                                    confirmPassword = ""
                                } else {
                                    snackbarHostState.showSnackbar(result.exceptionOrNull()?.message ?: "Gagal mengubah password.")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A3D))
                    ) {
                        Text("Simpan")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Data Privasi",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Data Anda tersimpan secara aman di dalam sistem kami. Kami tidak pernah membagikan data progres belajar Anda kepada pihak ketiga tanpa izin.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5f
            )
        }
    }
}
