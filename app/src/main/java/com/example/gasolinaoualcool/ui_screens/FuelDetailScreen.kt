package com.example.gasolinaoualcool.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gasolinaoualcool.R
import com.example.gasolinaoualcool.data.FuelStation
import com.example.gasolinaoualcool.data.FuelRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelDetailScreen(navController: NavController, stationId: String, context: Context) {
    val repository = remember { FuelRepository(context) }
    val station = repository.getStationById(stationId) ?: return

    var name by remember { mutableStateOf(station.name) }
    var alcoholPrice by remember { mutableStateOf(station.alcoholPrice.toString()) }
    var gasPrice by remember { mutableStateOf(station.gasPrice.toString()) }
    var latitude by remember { mutableStateOf(station.latitude?.toString() ?: "Não disponível") }
    var longitude by remember { mutableStateOf(station.longitude?.toString() ?: "Não disponível") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.station_details))})
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.station_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = alcoholPrice,
                onValueChange = { alcoholPrice = it },
                label = { Text(stringResource(id = R.string.alcohol_price)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = gasPrice,
                onValueChange = { gasPrice = it },
                label = { Text(stringResource(id = R.string.gas_price)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Apenas exibir a latitude e longitude, pois elas são obtidas automaticamente
            Text(text = "Latitude: $latitude", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Longitude: $longitude", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                repository.updateStation(
                    FuelStation(
                        id = station.id,
                        name = name,
                        alcoholPrice = alcoholPrice.toFloatOrNull() ?: station.alcoholPrice,
                        gasPrice = gasPrice.toFloatOrNull() ?: station.gasPrice,
                        date = station.date, // Mantém a data original
                        latitude = station.latitude,
                        longitude = station.longitude
                    )
                )
                navController.popBackStack()
            }) {
                Text(stringResource(id = R.string.update))
            }

            Button(
                onClick = {
                    repository.deleteStation(stationId)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(id = R.string.delete))            }
        }
    }
}
