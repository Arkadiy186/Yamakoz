package com.example.yamakoz.ui.cheque

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import com.example.yamakoz.ui.navigation.ShowData

@Composable
fun ChequeRoute(model: ChequeViewModel) {
    ShowData(model.receipt) { receipt ->
        if (receipt == null) Text("Чек не найден") else ChequeScreen(receipt.first, receipt.second)
    }
}
