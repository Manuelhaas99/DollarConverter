package com.example.dollarconverter.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dollarconverter.viewmodel.DollarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DollarConverter(viewModel: DollarViewModel) {
    // Variable to extract the UI values from the viewmodel
    val uiState by viewModel.uiState.collectAsState()

    var inputMeasure by remember { mutableStateOf("") }
    var inputTemp by remember { mutableStateOf("") }
    var inputPound by remember { mutableStateOf("") }
    var feetConverted by remember { mutableStateOf("") }
    var tempConverted by remember { mutableStateOf("") }
    var poundConverted by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(0) }
    var measureSelectedIndex by remember { mutableStateOf(0) }
    var tempSelectedIndex by remember { mutableStateOf(0) }
    var poundSelectedIndex by remember { mutableStateOf(0) }
    val options = listOf("USD To MXN", "MXN To USD")
    val measureOptions = listOf("Ft to M", "M to Ft", "Inch to Cm", "CM to Inch")
    val tempOptions = listOf("Fahrenheit To Celsius", "Celsius To Fahrenheit")
    val poundOptions = listOf("Pounds to KG", "KG to Pounds ")
    val labelText = when (measureSelectedIndex) {
        0 -> "Pies"
        1 -> "Metros"
        2 -> "Pulgadas"
        3 -> "Centímetros"
        else -> ""
    }
    LaunchedEffect(measureSelectedIndex, inputMeasure) {
        if (inputMeasure.isNotBlank()) {
            feetConverted = try {
                val measure = inputMeasure.toDouble()
                if (measure < 0) {
                    "No se permiten numeros negativos"
                } else if (measure > 0) {
                    when (measureSelectedIndex) {
                        0 -> "%.4f Metros".format(measure * 0.3048)
                        1 -> "%.4f Pies".format(measure / 0.3048)
                        2 -> "%.2f Centimeters".format(measure * 2.54)
                        3 -> "%.6f Inches".format(measure / 2.54)
                        else -> ""
                    }
                } else {
                    "Error al obtener la medida"
                }
            } catch (e: Exception) {
                "Entrada invalida"
            }
        } else {
            feetConverted = ""
        }
    }
    LaunchedEffect(tempSelectedIndex, inputTemp) {
        if (inputTemp.isNotBlank()) {
            tempConverted = try {
                val temp = inputTemp.toDouble()
                if (temp < 0) {
                    "No se permiten numeros negativos"
                } else if (temp > 0) {
                    when (tempSelectedIndex) {
                        0 -> "%.0f° Celsius".format((temp - 32) * 5 / 9)
                        1 -> "%.0f° Fahrenheit".format((temp * 9 / 5) + 32)
                        else -> ""
                    }
                } else {
                    "Error al obtener la temperatura"
                }
            } catch (e: Exception) {
                "Entrada invalida"
            }
        } else {
            tempConverted = ""
        }
    }
    LaunchedEffect(poundSelectedIndex, inputPound) {
        if (inputPound.isNotBlank()) {
            poundConverted = try {
                val peso = inputPound.toDouble()
                if (peso < 0) {
                    "No se permiten numeros negativos"
                } else if (peso > 0) {
                    when (poundSelectedIndex) {
                        0 -> "%.5ghf KG".format(peso * 0.4536)
                        1 -> "%.5f Pounds".format(peso  * 2.20462 )
                        else -> ""
                    }
                } else {
                    "Error al obtener la temperatura"
                }
            } catch (e: Exception) {
                "Entrada invalida"
            }
        } else {
            poundConverted = ""
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("1 USD = %.2f MXN".format(uiState.currencyRate))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        label = { Text(label) }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.currencyInput,
                onValueChange = { viewModel.handleCurrencyChange(it, selectedIndex) },
                label = { Text(if (selectedIndex == 0) "Dólares (USD)" else "Pesos (MXN)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(uiState.currencyValue, fontSize = 16.sp)


            //Feet to Meters
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                measureOptions.forEachIndexed { index, label ->
                    SegmentedButton(
                        modifier = Modifier
                            .weight(1f),
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = measureOptions.size
                        ),
                        onClick = { measureSelectedIndex = index },
                        selected = index == measureSelectedIndex,
                        label = { Text(text = label, maxLines = 1, softWrap = false) }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputMeasure,
                onValueChange = { inputMeasure = it },
                label = { Text(labelText) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(feetConverted, fontSize = 16.sp)

            //Farenheit to Celsius
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                tempOptions.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = tempOptions.size
                        ),
                        onClick = { tempSelectedIndex = index },
                        selected = index == tempSelectedIndex,
                        label = { Text(label) }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputTemp,
                onValueChange = { inputTemp = it },
                label = { Text(if (tempSelectedIndex == 0) "Fahrenheit" else "Celsius") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(tempConverted, fontSize = 16.sp)

            //Libras a KG
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                poundOptions.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = poundOptions.size
                        ),
                        onClick = { poundSelectedIndex = index },
                        selected = index == poundSelectedIndex,
                        label = { Text(label) }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputPound,
                onValueChange = { inputPound = it },
                label = { Text(if (poundSelectedIndex == 0) "Pounds" else "KG") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(poundConverted, fontSize = 16.sp)
        }
    }
}