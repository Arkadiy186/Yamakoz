package com.example.yamakoz.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yamakoz.data.local.CartItem
import com.example.yamakoz.ui.catalog.priceText

@Composable
fun CartScreen(items: List<CartItem>, busy: Boolean, remove: (Long) -> Unit, checkout: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (items.isEmpty()) Text("Корзина пуста")
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.productId }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium)
                        Text("${item.quantity} шт. · ${priceText(item.price * item.quantity)}")
                        TextButton(
                            onClick = { remove(item.productId) },
                            enabled = !busy
                        ) { Text("Удалить") }
                    }
                }
            }
        }
        Text("Итого: ${priceText(items.sumOf { it.price * it.quantity })}")
        Button(onClick = checkout, enabled = !busy && items.isNotEmpty()) { Text("Оформить заказ") }
    }
}
