package com.example.yamakoz.ui.order

import androidx.compose.runtime.Composable
import com.example.yamakoz.ui.navigation.ObserveEffects

@Composable
fun OrderSearchRoute(model: OrderSearchViewModel, message: (String) -> Unit, found: (Long) -> Unit) {
    ObserveEffects(model, message, found)
    OrderSearchScreen(model::find)
}
