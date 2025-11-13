package com.example.devclicker.ui.game.clicker

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.navigation.NavHostController
import com.example.devclicker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale


data class FloatingTextInfo(
    val id: UUID = UUID.randomUUID(),
    val text: String
)

@Composable
fun ClickerScreen(
    navController: NavHostController,
    viewModel: ClickerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val floatingTexts = remember { mutableStateListOf<FloatingTextInfo>() }
    val consoleListState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1.0f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState.consoleLines.size) {
        if (uiState.consoleLines.isNotEmpty()) {
            consoleListState.animateScrollToItem(uiState.consoleLines.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format("DevPoints: %.1f", uiState.pontos),
            fontSize = 32.sp,
            color = Color.White
        )
        Text(
            text = String.format("Pontos p/ segundo: %.1f", uiState.pontosPorSegundo),
            fontSize = 16.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        scope.launch {
                            scale.animateTo(0.9f, animationSpec = tween(100)) // Diminui
                            scale.animateTo(1.0f, animationSpec = tween(100)) // Volta
                        }
                        viewModel.onDevClicked()
                        floatingTexts.add(FloatingTextInfo(text = "+${uiState.pontosPorClique}"))
                    }
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .scale(scale.value)
            )

            floatingTexts.forEach { textInfo ->
                FloatingText(
                    textInfo = textInfo,
                    onAnimationEnd = {
                        floatingTexts.remove(textInfo)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            LazyColumn(
                state = consoleListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.consoleLines) { line ->
                    Text(
                        text = "> $line",
                        color = Color(0xFF00C853),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingText(
    textInfo: FloatingTextInfo,
    onAnimationEnd: () -> Unit
) {
    val offsetY = remember { Animatable(-50f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(key1 = textInfo.id) {
        launch {
            offsetY.animateTo(
                targetValue = -150f,
                animationSpec = tween(durationMillis = 800)
            )
        }
        launch {
            delay(400)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400)
            )
        }
        delay(800)
        onAnimationEnd()
    }

    Text(
        text = textInfo.text,
        modifier = Modifier
            .offset(y = offsetY.value.dp)
            .alpha(alpha.value),
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium.copy(
            shadow = androidx.compose.ui.graphics.Shadow(
                color = Color.Black,
                blurRadius = 4f
            )
        )
    )
}