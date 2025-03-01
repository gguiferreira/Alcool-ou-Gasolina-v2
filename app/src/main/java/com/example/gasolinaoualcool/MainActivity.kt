package com.example.gasolinaoualcool

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.gasolinaoualcool.data.FuelRepository
import com.example.gasolinaoualcool.data.FuelStation
import com.example.gasolinaoualcool.ui.navigation.NavGraph
import com.example.gasolinaoualcool.ui.theme.GasolinaOuAlcoolTheme
import com.example.gasolinaoualcool.utils.LocationHelper
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            GasolinaOuAlcoolTheme {
                NavGraph(navController, this)
            }
        }
    }
}

@Composable
fun GasolinaOuAlcoolScreen(context: Context, onNavigateToList: () -> Unit) {
    val fuelRepository = remember { FuelRepository(context) }
    var name by remember { mutableStateOf("") }
    var alcoolPrice by remember { mutableStateOf("") }
    var gasPrice by remember { mutableStateOf("") }
    var userLatitude by remember { mutableStateOf<Double?>(null) }
    var userLongitude by remember { mutableStateOf<Double?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val location = LocationHelper.getCurrentLocation(context)
            userLatitude = location?.first
            userLongitude = location?.second
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            val location = LocationHelper.getCurrentLocation(context)
            userLatitude = location?.first
            userLongitude = location?.second
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.add_station),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = {  Text(stringResource(id = R.string.station_name))  },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = alcoolPrice,
                onValueChange = { alcoolPrice = it },
                label = { Text(stringResource(id = R.string.alcohol_price)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = gasPrice,
                onValueChange = { gasPrice = it },
                label = {Text(stringResource(id = R.string.gas_price))},
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Row {
                Button(
                    onClick = {
                        val alcool = alcoolPrice.toFloatOrNull() ?: return@Button
                        val gasolina = gasPrice.toFloatOrNull() ?: return@Button
                        if (name.isNotEmpty()) {
                            val newStation = FuelStation(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                alcoholPrice = alcool,
                                gasPrice = gasolina,
                                date = System.currentTimeMillis().toString(),
                                latitude = userLatitude,
                                longitude = userLongitude
                            )
                            fuelRepository.addStation(newStation, context)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.save))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onNavigateToList,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.list_stations))                }
            }
        }
    }
}