package com.example.yamakoz.ui.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yamakoz.ui.navigation.ObserveEffects
import com.example.yamakoz.ui.navigation.ShowData

@Composable
fun CartRoute(model: CartViewModel, message: (String) -> Unit, orderReady: (Long) -> Unit) {
    val busy by model.busy.collectAsStateWithLifecycle()
    ObserveEffects(model, message, orderReady)
    ShowData(model.items) { CartScreen(it, busy, model::remove, model::checkout) }
}
