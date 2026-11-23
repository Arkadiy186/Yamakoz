package com.example.yamakoz.ui.order

import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

class OrderSearchViewModel(
    private val repository: ShopRepository,
    private val customerId: Long,
) : ActionViewModel<Long>() {
    fun find(id: Long) = execute {
        if (repository.order(customerId, id).first() == null) ScreenEffect.Message("Заказ не найден")
        else ScreenEffect.Completed(id)
    }
}
