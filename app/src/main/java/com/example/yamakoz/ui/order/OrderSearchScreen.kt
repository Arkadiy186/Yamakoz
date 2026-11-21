package com.example.yamakoz.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OrderSearchScreen(find: (Long) -> Unit) {
    var number by rememberSaveable { mutableStateOf("") }
    val id = number.toLongOrNull()?.takeIf { it > 0 }
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(number, { number = it.filter { char -> char in '0'..'9' } },
            Modifier.fillMaxWidth(), label = { Text("Номер заказа") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
        Button(onClick = { id?.let(find) }, enabled = id != null) { Text("Найти заказ") }
    }
}
