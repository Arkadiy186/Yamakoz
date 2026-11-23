package com.example.yamakoz.ui.catalog

import androidx.compose.runtime.Composable
import com.example.yamakoz.ui.navigation.ShowData

@Composable
fun CatalogRoute(model: CatalogViewModel, select: (Long) -> Unit) {
    ShowData(model.categories) { CatalogScreen(it, select) }
}
