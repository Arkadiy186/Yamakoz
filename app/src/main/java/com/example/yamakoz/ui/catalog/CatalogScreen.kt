package com.example.yamakoz.ui.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yamakoz.data.local.entity.Category

@Composable
fun CatalogScreen(categories: List<Category>, select: (Long) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories, key = { it.id }) { category ->
            OutlinedButton(onClick = { select(category.id) }, modifier = Modifier.fillMaxWidth()) {
                Text(category.name)
            }
        }
    }
}
