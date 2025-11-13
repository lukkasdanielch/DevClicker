package com.example.devclicker.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.devclicker.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.devclicker.ui.theme.MatrixBackground
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val loginState by viewModel.loginState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val matrixGreen = Color(0xFF00C853)

    Box(modifier = Modifier.fillMaxSize()) {
        MatrixBackground()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
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
                        modifier = Modifier.size(120.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedIndicatorColor = matrixGreen,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = matrixGreen
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedIndicatorColor = matrixGreen,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = matrixGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.login(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = matrixGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Entrar")
                    }

                    TextButton(
                        onClick = { navController.navigate("signup_screen") },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Não tem conta? Cadastre-se")
                    }

                    when (loginState) {
                        is LoginState.Loading -> CircularProgressIndicator()
                        is LoginState.Error -> {
                            val message = (loginState as LoginState.Error).message
                            LaunchedEffect(message) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        }

                        is LoginState.Success -> {
                            LaunchedEffect(Unit) {
                                navController.navigate("game_screen") {
                                    popUpTo("login_screen") { inclusive = true }
                                }
                            }
                        }

                        else -> {}
                    }

                    SnackbarHost(hostState = snackbarHostState)
                }
            }
        }
    }
}