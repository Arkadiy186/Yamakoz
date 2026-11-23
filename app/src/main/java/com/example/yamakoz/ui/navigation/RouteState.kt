package com.example.yamakoz.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.yamakoz.ui.ActionViewModel
import com.example.yamakoz.ui.ScreenData
import com.example.yamakoz.ui.ScreenEffect
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun <T> ShowData(source: StateFlow<ScreenData<T>>, content: @Composable (T) -> Unit) {
    when (val state = source.collectAsStateWithLifecycle().value) {
        ScreenData.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        ScreenData.Failed -> Text("Не удалось загрузить данные", Modifier.padding(16.dp))
        is ScreenData.Ready -> content(state.value)
    }
}

@Composable
internal fun <T> ObserveEffects(model: ActionViewModel<T>, message: (String) -> Unit, complete: (T) -> Unit) {
    val owner = LocalLifecycleOwner.current
    val showMessage by rememberUpdatedState(message)
    val onComplete by rememberUpdatedState(complete)
    LaunchedEffect(model, owner) {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            model.effects.collect { effect ->
                when (effect) {
                    is ScreenEffect.Message -> showMessage(effect.text)
                    is ScreenEffect.Completed -> onComplete(effect.result)
                }
            }
        }
    }
}
