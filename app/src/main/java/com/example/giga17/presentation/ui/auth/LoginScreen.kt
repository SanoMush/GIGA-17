package com.example.giga17.presentation.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.R
import com.example.giga17.presentation.viewmodel.AuthState
import com.example.giga17.presentation.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var nis by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    val primaryOrange = Color(0xFFFF7A3D)
    val lightBackground = Color(0xFFFCFDFD)

    // Gradient background to mimic the subtle wavy orange background in the image
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            lightBackground,
            Color(0xFFFFF5F0)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Outside the Box
            Text(
                text = "GIGA 17",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black
                ),
                color = primaryOrange,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gamified Informatics Guide &\nAcademy for 17",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            // 3D Illustration Image
            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = "Login Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(vertical = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 16.dp, 
                        shape = RoundedCornerShape(24.dp), 
                        ambientColor = Color.Black.copy(alpha = 0.05f), 
                        spotColor = Color.Black.copy(alpha = 0.05f)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Welcome Message inside the Box
                    Text(
                        text = "Selamat Datang!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = primaryOrange,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Masuk untuk melanjutkan belajar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 32.dp),
                        textAlign = TextAlign.Center
                    )

                    // Input NIS
                    OutlinedTextField(
                        value = nis,
                        onValueChange = { nis = it },
                        placeholder = { Text("NIS", color = Color.Gray) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon", tint = primaryOrange)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password", color = Color.Gray) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon", tint = primaryOrange)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Error Message
                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Login Button
                    Button(
                        onClick = { viewModel.login(nis, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = authState !is AuthState.Loading && nis.isNotBlank() && password.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryOrange,
                            disabledContainerColor = primaryOrange.copy(alpha = 0.5f)
                        )
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                text = "LOGIN",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
