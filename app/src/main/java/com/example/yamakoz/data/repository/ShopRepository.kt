package com.example.yamakoz.data.repository

import androidx.room.withTransaction
import com.example.yamakoz.data.local.AppDatabase
import com.example.yamakoz.data.local.entity.*
import kotlinx.coroutines.flow.map

class ShopRepository(private val database: AppDatabase) {
    private val dao = database.shopDao()

    fun categories() = dao.categories()

    fun products(categoryId: Long, search: String) = dao.products(categoryId).map { products ->
        products.filter { it.name.contains(search.trim(), ignoreCase = true) }
    }

    fun cart(customerId: Long) = dao.cart(customerId)

    fun order(customerId: Long, orderId: Long) = dao.order(customerId, orderId)

    fun cheque(customerId: Long, orderId: Long) = dao.cheque(customerId, orderId)

    suspend fun signIn(input: String, name: String, email: String?): Customer = database.withTransaction {
        require(input.all { it in '0'..'9' || it in "+()- " }) { "Введите номер телефона" }
        val digits = input.filter { it in '0'..'9' }
        val national = when {
            digits.length == 10 -> digits
            digits.length == 11 && digits.first() in "78" -> digits.drop(1)
            else -> throw IllegalArgumentException("Введите российский номер телефона")
        }
        val phone = "+7$national"
        dao.customer(phone)?.let { return@withTransaction it }
        val customer = Customer(name = name.trim().ifEmpty { "Клиент" }, phone = phone,
            email = email?.trim()?.takeIf { it.isNotEmpty() }, registrationDate = System.currentTimeMillis())
        customer.copy(id = dao.insertCustomer(customer))
    }

    suspend fun add(customerId: Long, productId: Long) = dao.add(customerId, productId)

    suspend fun remove(customerId: Long, productId: Long) = dao.remove(customerId, productId)

    suspend fun checkout(customerId: Long): Long = database.withTransaction {
        val items = dao.readCart(customerId)
        check(items.isNotEmpty()) { "Корзина пуста" }
        val orderId = dao.insertOrder(Order(customerId = customerId, orderDate = System.currentTimeMillis(),
            structure = items.joinToString("\n") { "${it.name} × ${it.quantity}" },
            amount = items.sumOf { it.price * it.quantity }, status = "awaiting_payment"))
        for (item in items) {
            check(dao.deductStock(item.productId, item.quantity) == 1) { "Недостаточный остаток: ${item.name}" }
        }
        dao.clearCart(customerId)
        orderId
    }

    suspend fun pay(customerId: Long, orderId: Long) = database.withTransaction {
        val order = checkNotNull(dao.readOrder(customerId, orderId)) { "Заказ не найден" }
        check(dao.markPaid(customerId, orderId) == 1) { "Заказ уже оплачен или отменён" }
        dao.insertCheque(Cheque(orderId, customerId, System.currentTimeMillis(), order.amount))
    }
}
