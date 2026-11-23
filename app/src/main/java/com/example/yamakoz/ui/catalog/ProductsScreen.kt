package com.example.yamakoz.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yamakoz.data.local.entity.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

internal fun priceText(kopecks: Long): String =
    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("ru-RU"))
        .format(BigDecimal.valueOf(kopecks, 2))

@Composable
fun ProductsScreen(
    products: List<Product>?,
    search: String,
    onSearch: (String) -> Unit,
    add: (Long) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            search,
            onSearch,
            Modifier.fillMaxWidth(),
            label = { Text("Поиск по названию") },
            singleLine = true
        )
        if (products == null) {
            CircularProgressIndicator()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (products.isEmpty()) item { Text("Товары не найдены") }
                items(products, key = { it.id }) { product ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text(priceText(product.price))
                            Text("В наличии: ${product.stock}")
                            Button(
                                onClick = { add(product.id) },
                                enabled = product.stock > 0
                            ) { Text("В корзину") }
                        }
                    }
                }
            }
        }
    }
}
