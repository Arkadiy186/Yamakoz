package com.example.yamakoz.ui.cart

import androidx.lifecycle.viewModelScope
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

class CartViewModel(private val repository: ShopRepository, private val customerId: Long) : ActionViewModel<Long>() {
    val items = repository.cart(customerId).screenData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScreenData.Loading)

    fun remove(productId: Long) = execute {
        repository.remove(customerId, productId)
        null
    }

    fun checkout() = execute { ScreenEffect.Completed(repository.checkout(customerId)) }
}
