package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class Order(val id: String, val date: String, val total: String, val status: String)

@HiltViewModel
class OrdersViewModel @Inject constructor() : ViewModel() {
    private val _orders = MutableStateFlow(
        listOf(
            Order("ORD-7721", "24 Mai 2024", "450 DH", "En cours"),
            Order("ORD-6542", "12 Mai 2024", "1,200 DH", "Livré"),
            Order("ORD-5501", "01 Mai 2024", "300 DH", "Livré")
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
}
