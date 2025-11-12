package com.example.devclicker.ui.game.clicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devclicker.R

@Composable
fun ClickerScreen(
    viewModel: ClickerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("DevPoints: ${uiState.pontos}", fontSize = 32.sp)
        Text("Pontos p/ segundo: ${uiState.pontosPorSegundo}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .clickable { viewModel.onDevClicked() }
        )
    }
}