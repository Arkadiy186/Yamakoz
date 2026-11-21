package com.example.yamakoz.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yamakoz.data.local.entity.Order
import com.example.yamakoz.ui.catalog.priceText

@Composable
fun OrderScreen(order: Order, phone: String, busy: Boolean, pay: () -> Unit, viewCheque: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Заказ №${order.id}", style = MaterialTheme.typography.headlineSmall)
        Text("Телефон: $phone")
        Text("Пункт выдачи: Магазин бытовой техники, г. Казань")
        Text(order.structure)
        Text("Итого: ${priceText(order.amount)}")
        when (order.status) {
            "awaiting_payment" -> Button(onClick = pay, enabled = !busy) { Text("Оплатить онлайн") }
            "paid" -> Button(onClick = viewCheque) { Text("Открыть чек") }
            "cancelled" -> Text("Заказ отменён")
        }
    }
}
