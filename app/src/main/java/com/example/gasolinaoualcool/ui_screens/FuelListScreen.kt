package com.example.gasolinaoualcool.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gasolinaoualcool.R
import com.example.gasolinaoualcool.data.FuelRepository
import com.example.gasolinaoualcool.data.FuelStation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelListScreen(context: Context, navController: NavController) {
    val repository = remember { FuelRepository(context) }
    var fuelStations by remember { mutableStateOf(repository.getStations()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.list_stations)) })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(fuelStations) { station ->
                    FuelStationItem(
                        station,
                        onDelete = {
                            repository.deleteStation(station.id)
                            fuelStations = repository.getStations()
                        },
                        onClick = {
                            navController.navigate("fuel_detail/${station.id}")
                        },
                        onShowMap = {
                            station.latitude?.let { lat ->
                                station.longitude?.let { lon ->
                                    val uri = "geo:$lat,$lon?q=$lat,$lon(${station.name})"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                                    intent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(intent)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FuelStationItem(
    station: FuelStation,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onShowMap: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.station_name) + ": ${station.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(id = R.string.alcohol_price) + ": R$ ${station.alcoholPrice}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.gas_price) + ": R$ ${station.gasPrice}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.date) + ": ${station.date}",
                style = MaterialTheme.typography.bodySmall
            )


            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { onClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(id = R.string.station_details))
                }

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(id = R.string.delete))                }
            }

            if (station.latitude != null && station.longitude != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onShowMap,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(stringResource(id = R.string.location))                }
            }
        }
    }
}
