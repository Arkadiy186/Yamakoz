package com.example.yamakoz.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.yamakoz.data.local.entity.Customer
import com.example.yamakoz.ui.navigation.ObserveEffects

@Composable
fun AuthRoute(model: AuthViewModel, message: (String) -> Unit, signedIn: (Customer) -> Unit) {
    val busy by model.busy.collectAsStateWithLifecycle()
    ObserveEffects(model, message, signedIn)
    AuthScreen(busy, model::signIn)
}
