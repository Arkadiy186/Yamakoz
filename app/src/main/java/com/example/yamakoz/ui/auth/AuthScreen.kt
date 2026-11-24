package com.example.yamakoz.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(busy: Boolean, signIn: (String, String) -> Unit) {
    var phone by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            phone,
            { phone = nationalNumber(it, phone) },
            Modifier.fillMaxWidth(),
            label = { Text("Телефон") },
            prefix = { Text(PhonePrefix) },
            visualTransformation = PhoneMask,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
        OutlinedTextField(
            name,
            { name = it },
            Modifier.fillMaxWidth(),
            label = { Text("Имя (не обязательно)") },
            singleLine = true
        )
        Button(onClick = { signIn("+7$phone", name) }, enabled = !busy && phone.length == NationalNumberLength) {
            Text("Продолжить")
        }
    }
}

private const val PhonePrefix = "+7-"
private const val NationalNumberLength = 10

private val PhoneMask = VisualTransformation { input ->
    val digits = input.text
    val formatted = buildString {
        digits.forEachIndexed { index, digit ->
            if (index == 3 || index == 6 || index == 8) append('-')
            append(digit)
        }
    }
    val offsets = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            offset + listOf(3, 6, 8).count { it < offset }

        override fun transformedToOriginal(offset: Int): Int =
            formatted.take(offset).count { it != '-' }
    }
    TransformedText(AnnotatedString(formatted), offsets)
}

private fun nationalNumber(input: String, current: String): String {
    val digits = input.filter { it in '0'..'9' }
    val withoutCountryCode = when {
        input.trimStart().startsWith("+7") -> digits.drop(1)
        current.isEmpty() && digits.length == 11 && digits.first() in "78" -> digits.drop(1)
        else -> digits
    }
    return withoutCountryCode.take(NationalNumberLength)
}
