package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class Address(val id: String, val label: String, val fullAddress: String)

@HiltViewModel
class AddressesViewModel @Inject constructor() : ViewModel() {
    private val _addresses = MutableStateFlow(
        listOf(
            Address("1", "Domicile", "123 Rue de la Koutoubia, Marrakech, 40000"),
            Address("2", "Bureau", "Technopark, Casablanca, 20150")
        )
    )
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    fun removeAddress(addressId: String) {
        _addresses.update { list -> list.filter { it.id != addressId } }
    }

    fun addAddress(label: String, address: String) {
        val newAddress = Address(
            id = (System.currentTimeMillis()).toString(),
            label = label,
            fullAddress = address
        )
        _addresses.update { it + newAddress }
    }
}
