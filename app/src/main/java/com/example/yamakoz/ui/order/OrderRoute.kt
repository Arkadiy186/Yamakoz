package com.example.yamakoz.ui.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Text
import com.example.yamakoz.ui.navigation.ObserveEffects
import com.example.yamakoz.ui.navigation.ShowData

@Composable
fun OrderRoute(model: OrderViewModel, phone: String, message: (String) -> Unit, showCheque: () -> Unit) {
    val busy by model.busy.collectAsStateWithLifecycle()
    ObserveEffects(model, message) { showCheque() }
    ShowData(model.order) { order ->
        if (order == null) Text("Заказ не найден") else OrderScreen(order, phone, busy, model::pay, showCheque)
    }
}
