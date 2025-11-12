package com.example.devclicker.ui.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.devclicker.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import kotlinx.coroutines.launch // 1. (Provável que esteja faltando) Importe o 'launch'

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel() // Hilt vai criar o ViewModel
) {
    val signUpState by viewModel.signUpState.collectAsState()

    // 2. ADICIONE A VARIÁVEL 'nome'
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // (Para a Snackbar, que você não tinha, mas é necessária para o 'Error')
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Criar Conta", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))

            // 3. ADICIONE O CAMPO DE TEXTO PARA O 'nome'
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                // 4. A CHAMADA AGORA FUNCIONA (pois 'nome' existe)
                onClick = { viewModel.signUp(nome, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Cadastrar")
                }
            }

            TextButton(onClick = { navController.popBackStack() }, enabled = !isLoading) {
                Text("Já tem conta? Faça login")
            }

            // 5. Lógica de estado corrigida para usar 'isLoading' e Snackbar
            LaunchedEffect(signUpState) {
                when (val state = signUpState) {
                    is SignUpState.Loading -> {
                        isLoading = true
                    }
                    is SignUpState.Success -> {
                        isLoading = false
                        navController.navigate("login_screen") {
                            popUpTo("signup_screen") { inclusive = true }
                        }
                    }
                    is SignUpState.Error -> {
                        isLoading = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(state.message)
                        }
                    }
                    is SignUpState.Idle -> {
                        isLoading = false
                    }
                }
            }
        }

        // 6. Adicione a SnackbarHost no final
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}