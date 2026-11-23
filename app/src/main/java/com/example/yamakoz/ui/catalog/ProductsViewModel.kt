package com.example.yamakoz.ui.catalog

import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModel(
    private val repository: ShopRepository,
    private val saved: SavedStateHandle,
    private val customerId: Long,
    categoryId: Long,
) : ActionViewModel<Unit>() {
    val query = saved.getStateFlow("query", "")
    val products = query.flatMapLatest { repository.products(categoryId, it).screenData() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScreenData.Loading)

    fun search(value: String) { saved["query"] = value }

    fun add(productId: Long) = execute {
        repository.add(customerId, productId)
        ScreenEffect.Message("Товар добавлен в корзину")
    }
}
