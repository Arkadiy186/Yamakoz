package com.example.yamakoz.ui.cheque

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yamakoz.data.local.entity.Cheque
import com.example.yamakoz.data.local.entity.Order
import com.example.yamakoz.ui.catalog.priceText
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val receiptDate =
    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.forLanguageTag("ru-RU"))
        .withZone(ZoneId.of("Europe/Moscow"))

@Composable
fun ChequeScreen(order: Order, cheque: Cheque) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Заказ №${order.id}", style = MaterialTheme.typography.headlineSmall)
        Text("Дата: ${receiptDate.format(Instant.ofEpochMilli(cheque.dateIssued))}")
        HorizontalDivider()
        Text(order.structure)
        HorizontalDivider()
        Text("Итого: ${priceText(cheque.amount)}")
        Text("Способ оплаты: ${cheque.paymentMethod}")
    }
}
