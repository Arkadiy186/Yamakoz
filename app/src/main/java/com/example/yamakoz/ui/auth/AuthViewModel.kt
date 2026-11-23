package com.example.yamakoz.ui.auth

import com.example.yamakoz.data.local.entity.Customer
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.*

class AuthViewModel(private val repository: ShopRepository) : ActionViewModel<Customer>() {
    fun signIn(phone: String, name: String, email: String) = execute {
        ScreenEffect.Completed(repository.signIn(phone, name, email))
    }
}
