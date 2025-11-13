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
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.launch
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import com.example.devclicker.ui.theme.MatrixBackground

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val signUpState by viewModel.signUpState.collectAsState()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

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
                        text = "Criar Conta",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedIndicatorColor = matrixGreen,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = matrixGreen,
                            disabledTextColor = Color.Gray,
                            disabledLabelColor = Color.Gray
                        )
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedIndicatorColor = matrixGreen,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = matrixGreen,
                            disabledTextColor = Color.Gray,
                            disabledLabelColor = Color.Gray
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.LightGray,
                            focusedIndicatorColor = matrixGreen,
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = matrixGreen,
                            disabledTextColor = Color.Gray,
                            disabledLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.signUp(nome, email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = matrixGreen,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.DarkGray
                        )
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

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}