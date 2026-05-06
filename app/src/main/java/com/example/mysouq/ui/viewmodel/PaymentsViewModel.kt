package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PaymentMethod(val id: String, val type: String, val number: String)

@HiltViewModel
class PaymentsViewModel @Inject constructor() : ViewModel() {
    private val _payments = MutableStateFlow(
        listOf(
            PaymentMethod("1", "Visa", "**** **** **** 1234"),
            PaymentMethod("2", "Mastercard", "**** **** **** 5678")
        )
    )
    val payments: StateFlow<List<PaymentMethod>> = _payments.asStateFlow()

    fun removePayment(id: String) {
        _payments.update { list -> list.filter { it.id != id } }
    }
}
