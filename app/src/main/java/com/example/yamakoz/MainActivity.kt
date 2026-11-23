package com.example.yamakoz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yamakoz.data.local.AppDatabase
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.navigation.ShopNavHost
import com.example.yamakoz.ui.theme.YamakozTheme

class MainActivity : ComponentActivity() {
    private val dependencies by viewModels<ShopDependencies> {
        viewModelFactory { initializer { ShopDependencies(AppDatabase.create(application)) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { YamakozTheme { ShopNavHost(dependencies.repository) } }
    }
}

private class ShopDependencies(database: AppDatabase) : ViewModel() {
    val repository = ShopRepository(database)

    init {
        addCloseable { database.close() }
    }
}
