package com.example.devclicker.ui.game.clicker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devclicker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

// Data class para controlar cada texto flutuante
data class FloatingTextInfo(
    val id: UUID = UUID.randomUUID(),
    val text: String
)

@Composable
fun ClickerScreen(
    viewModel: ClickerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Lista para armazenar os textos flutuantes
    val floatingTexts = remember { mutableStateListOf<FloatingTextInfo>() }

    // Estado para o LazyColumn (console)
    val consoleListState = rememberLazyListState()

    // Efeito para rolar o console para baixo automaticamente
    LaunchedEffect(uiState.consoleLines.size) {
        if (uiState.consoleLines.isNotEmpty()) {
            consoleListState.animateScrollToItem(uiState.consoleLines.size - 1)
        }
    }

    // Layout da Tela
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Adiciona preenchimento geral
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // <-- ADICIONADO PARA CENTRALIZAR TUDO
    ) {
        // 1. Stats (Topo)
        Text(String.format("DevPoints: %.1f", uiState.pontos), fontSize = 32.sp)
        Text(String.format("Pontos p/ segundo: %.1f", uiState.pontosPorSegundo), fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Imagem Clicável
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    viewModel.onDevClicked()
                    // Adiciona um novo texto flutuante na lista
                    floatingTexts.add(FloatingTextInfo(text = "+${uiState.pontosPorClique}"))
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize()
            )

            // Renderiza cada texto flutuante na lista
            floatingTexts.forEach { textInfo ->
                FloatingText(
                    textInfo = textInfo,
                    onAnimationEnd = {
                        // Remove o texto da lista quando a animação acabar
                        floatingTexts.remove(textInfo)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Console
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Define altura fixa
                .background(Color.Black, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            LazyColumn(
                state = consoleListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.consoleLines) { line ->
                    Text(
                        text = "> $line", // Estilo de prompt
                        color = Color(0xFF00C853), // Verde "hacker"
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// Composable separado para o texto flutuante e sua animação
@Composable
fun FloatingText(
    textInfo: FloatingTextInfo,
    onAnimationEnd: () -> Unit
) {
    // Controladores de animação para offset (posição Y) e alpha (transparência)
    val offsetY = remember { Animatable(-50f) } // Começa um pouco acima do centro
    val alpha = remember { Animatable(1f) }

    // Dispara a animação quando o Composable aparece
    LaunchedEffect(key1 = textInfo.id) {
        // Animação de subida
        launch {
            offsetY.animateTo(
                targetValue = -150f, // Sobe 100 pixels
                animationSpec = tween(durationMillis = 800)
            )
        }

        // Animação de fade-out (desaparecer)
        launch {
            delay(400) // Começa a desaparecer na metade do caminho
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400)
            )
        }

        delay(800) // Duração total da animação
        onAnimationEnd() // Avisa que a animação terminou
    }

    Text(
        text = textInfo.text,
        modifier = Modifier
            .offset(y = offsetY.value.dp) // Aplica a animação de subida
            .alpha(alpha.value),          // Aplica a animação de fade-out
        color = Color.White, // Use a cor que preferir
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium.copy(
            // Adiciona uma "sombra" de texto para melhor legibilidade
            shadow = androidx.compose.ui.graphics.Shadow(
                color = Color.Black,
                blurRadius = 4f
            )
        )
    )
}