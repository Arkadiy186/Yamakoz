package com.example.yamakoz.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

class CatalogViewModel(repository: ShopRepository) : ViewModel() {
    val categories = repository.categories().screenData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScreenData.Loading)
}
