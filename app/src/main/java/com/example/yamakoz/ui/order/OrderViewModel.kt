package com.example.yamakoz.ui.order

import androidx.lifecycle.viewModelScope
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

class OrderViewModel(
    private val repository: ShopRepository,
    private val customerId: Long,
    private val orderId: Long,
) : ActionViewModel<Unit>() {
    val order = repository.order(customerId, orderId).screenData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScreenData.Loading)

    fun pay() = execute {
        repository.pay(customerId, orderId)
        ScreenEffect.Completed(Unit)
    }
}
