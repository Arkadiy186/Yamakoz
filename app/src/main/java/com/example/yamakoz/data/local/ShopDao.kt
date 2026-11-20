package com.example.yamakoz.data.local

import androidx.room.*
import com.example.yamakoz.data.local.entity.*
import kotlinx.coroutines.flow.Flow

data class CartItem(val productId: Long, val name: String, val price: Long, val quantity: Int)

@Dao
interface ShopDao {
    @Query("SELECT * FROM categories ORDER BY name")
    fun categories(): Flow<List<Category>>

    @Query("SELECT p.* FROM products p JOIN catalog c ON p.id = c.productId WHERE c.categoryId = :categoryId ORDER BY p.name")
    fun products(categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM customers WHERE phone = :phone")
    suspend fun customer(phone: String): Customer?

    @Insert
    suspend fun insertCustomer(customer: Customer): Long

    @Query("INSERT INTO cart VALUES (:customerId, :productId, 1) ON CONFLICT(customerId, productId) DO UPDATE SET quantity = quantity + 1")
    suspend fun add(customerId: Long, productId: Long): Long

    @Query("DELETE FROM cart WHERE customerId = :customerId AND productId = :productId")
    suspend fun remove(customerId: Long, productId: Long): Int

    @Query(CART)
    fun cart(customerId: Long): Flow<List<CartItem>>

    @Query(CART)
    suspend fun readCart(customerId: Long): List<CartItem>

    @Query("DELETE FROM cart WHERE customerId = :customerId")
    suspend fun clearCart(customerId: Long): Int

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :productId AND stock >= :quantity")
    suspend fun deductStock(productId: Long, quantity: Int): Int

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Query(ORDER)
    fun order(customerId: Long, orderId: Long): Flow<Order?>

    @Query(ORDER)
    suspend fun readOrder(customerId: Long, orderId: Long): Order?

    @Query("UPDATE orders SET status = 'paid' WHERE customerId = :customerId AND id = :orderId AND status = 'awaiting_payment'")
    suspend fun markPaid(customerId: Long, orderId: Long): Int

    @Insert
    suspend fun insertCheque(cheque: Cheque): Long

    @Query("SELECT * FROM cheques WHERE customerId = :customerId AND orderId = :orderId")
    fun cheque(customerId: Long, orderId: Long): Flow<Cheque?>
}

private const val CART = "SELECT p.id AS productId, p.name, p.price, c.quantity FROM cart c JOIN products p ON p.id = c.productId WHERE c.customerId = :customerId ORDER BY p.name"
private const val ORDER = "SELECT * FROM orders WHERE customerId = :customerId AND id = :orderId"
