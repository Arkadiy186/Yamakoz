package com.example.yamakoz.ui.cheque

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*
import kotlinx.coroutines.flow.*

class ChequeViewModel(repository: ShopRepository, customerId: Long, orderId: Long) : ViewModel() {
    val receipt = combine(repository.order(customerId, orderId), repository.cheque(customerId, orderId)) { order, cheque ->
        if (order == null || cheque == null) null else order to cheque
    }.screenData().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScreenData.Loading)
}
