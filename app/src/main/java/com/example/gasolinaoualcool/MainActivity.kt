package com.example.gasolinaoualcool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gasolinaoualcool.ui.theme.GasolinaOuAlcoolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GasolinaOuAlcoolTheme {
                GasolinaOuAlcoolScreen()
            }
        }
    }
}

@Composable
fun GasolinaOuAlcoolScreen() {
    var isSeventyPercent by remember { mutableStateOf(true) }
    var alcoolPrice by remember { mutableStateOf("") }
    var gasPrice by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }

    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Qual o combustível mais vantajoso",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 90.dp)

            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Usar 70%")
                Switch(
                    checked = isSeventyPercent,
                    onCheckedChange = { isSeventyPercent = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(text = "Usar 75%")
            }

            OutlinedTextField(
                value = alcoolPrice,
                onValueChange = { alcoolPrice = it },
                label = { Text("Preço do Álcool") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            // Campo de entrada para o preço da gasolina
            OutlinedTextField(
                value = gasPrice,
                onValueChange = { gasPrice = it },
                label = { Text("Preço da Gasolina") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Button(onClick = {
                val alcool = alcoolPrice.toFloatOrNull() ?: 0f
                val gasolina = gasPrice.toFloatOrNull() ?: 0f
                val threshold = if (isSeventyPercent) 0.7 else 0.75

                resultMessage = if (alcool <= gasolina * threshold) {
                    "Álcool é mais vantajoso!"
                } else {
                    "Gasolina é mais vantajosa!"
                }
            }) {
                Text(text = "Calcular")
            }
            if (resultMessage.isNotEmpty()) {
                Text(
                    text = resultMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlcoolOuGasolinaScreenPreview() {
    GasolinaOuAlcoolTheme {
        GasolinaOuAlcoolScreen()
    }
}
