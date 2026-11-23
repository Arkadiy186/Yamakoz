package com.example.yamakoz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface ScreenData<out T> {
    data object Loading : ScreenData<Nothing>
    data object Failed : ScreenData<Nothing>
    data class Ready<T>(val value: T) : ScreenData<T>
}

fun <T> Flow<T>.screenData(): Flow<ScreenData<T>> =
    map<T, ScreenData<T>> { ScreenData.Ready(it) }
        .onStart { emit(ScreenData.Loading) }
        .catch { emit(ScreenData.Failed) }

sealed interface ScreenEffect<out T> {
    data class Completed<T>(val result: T) : ScreenEffect<T>
    data class Message(val text: String) : ScreenEffect<Nothing>
}

abstract class ActionViewModel<T> : ViewModel() {
    private val running = MutableStateFlow(false)
    val busy = running.asStateFlow()
    private val outgoing = Channel<ScreenEffect<T>>(Channel.BUFFERED)
    val effects = outgoing.receiveAsFlow()

    protected fun execute(action: suspend () -> ScreenEffect<T>?) {
        if (!running.compareAndSet(false, true)) return
        viewModelScope.launch {
            try {
                action()?.let { outgoing.send(it) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: IllegalArgumentException) {
                outgoing.send(ScreenEffect.Message(error.message ?: "Проверьте введённые данные"))
            } catch (error: IllegalStateException) {
                outgoing.send(ScreenEffect.Message(error.message ?: "Операция недоступна"))
            } catch (_: Exception) {
                outgoing.send(ScreenEffect.Message("Не удалось выполнить операцию"))
            } finally {
                running.value = false
            }
        }
    }
}
