package com.example.yamakoz.ui.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Text
import com.example.yamakoz.ui.ScreenData
import com.example.yamakoz.ui.navigation.ObserveEffects

@Composable
fun ProductsRoute(model: ProductsViewModel, message: (String) -> Unit) {
    val query by model.query.collectAsStateWithLifecycle()
    val products by model.products.collectAsStateWithLifecycle()
    ObserveEffects(model, message) {}
    val loaded = products
    if (loaded == ScreenData.Failed) {
        Text("Не удалось загрузить товары")
    } else {
        ProductsScreen((loaded as? ScreenData.Ready)?.value, query, model::search, model::add)
    }
}
