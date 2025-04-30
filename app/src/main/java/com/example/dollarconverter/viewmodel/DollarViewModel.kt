package com.example.dollarconverter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dollarconverter.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DollarUIData(
  val currencyValue: String = "",
  val currencyInput: String = "",
  val currencyRate: Double = 0.0,
  val currencyFetchError: String = "",
  val isInputError: Boolean = false,
  val currencyInputError: String = ""
)

/**
 * Viewmodel that will handle all the UI logic
 */
class DollarViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(DollarUIData())
  val uiState = _uiState.asStateFlow()

  init {
    fetchDollarToMXNRate()
    _uiState.update { it.copy(currencyValue = 0.0.toFormattedString()) }
  }

  fun handleCurrencyChange(newValue: String, selectedIndex: Int) {
    if (newValue.isEmpty()) {
      _uiState.update { it.copy(currencyInput = "", currencyValue = 0.0.toFormattedString()) }
      return
    }

    val doubleValue = runCatching { newValue.toDouble() }.getOrElse { 0.0 }

    if (_uiState.value.isInputError && doubleValue != 0.0) {
      _uiState.update { it.copy(isInputError = false) }
    }

    if (doubleValue < 0) {
      _uiState.update { it.copy(currencyValue = "No se permiten numero negativos") }
      return
    }

    if (uiState.value.currencyRate > 0 && doubleValue != 0.0) {
      val calculatedValue = when (selectedIndex) {
        0 -> doubleValue * uiState.value.currencyRate
        1 -> doubleValue / uiState.value.currencyRate
        else -> 0.0
      }
      _uiState.update { state ->
        state.copy(
          currencyValue = calculatedValue.toFormattedString(),
          currencyInput = newValue
        )
      }
      return
    }

    // This should be moved to error handling logic and leave the currency value as 0
    _uiState.update {
      it.copy(
        isInputError = true,
        currencyInputError = "Solo numeros son validos"
      )
    }
  }

  fun fetchDollarToMXNRate() {
    viewModelScope.launch {
      try {
        val result = RetrofitClient.service.getDollarRates()
        _uiState.update { it.copy(currencyRate = result.conversion_rates.MXN) }
      } catch (e: Exception) {
        _uiState.update { it.copy(currencyRate = 0.0, currencyFetchError = "Error al conectarse") }
      }
    }
  }

  private fun Double.toFormattedString() = "$%.2f".format(this)
  fun resetInputError() {
    _uiState.update { it.copy(isInputError = false) }
  }

}