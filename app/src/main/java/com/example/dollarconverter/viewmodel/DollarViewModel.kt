package com.example.dollarconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dollarconverter.data.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DollarViewModel : ViewModel() {
    private val _mxnRate = MutableStateFlow(0.00)
    val mxnRate = _mxnRate.asStateFlow()

    fun fetchDollarToMXNRate() {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.service.getDollarRates()
                _mxnRate.value = result.conversion_rates.MXN
            } catch (e: Exception) {
                _mxnRate.value = -1.0
            }
        }
    }
}