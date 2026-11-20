package com.example.yamakoz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(
    tableName = "products",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"]
    )],
    indices = [Index("categoryId")],
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryId: Long,
    val price: Long,
    val stock: Int,
)

@Entity(
    tableName = "catalog",
    primaryKeys = ["productId", "categoryId"],
    foreignKeys = [
        ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"]),
        ForeignKey(entity = Category::class, parentColumns = ["id"], childColumns = ["categoryId"]),
    ],
    indices = [Index("categoryId")],
)
data class CatalogEntry(val productId: Long, val categoryId: Long)

@Entity(tableName = "customers", indices = [Index(value = ["phone"], unique = true)])
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String?,
    val registrationDate: Long,
)

@Entity(
    tableName = "cart",
    primaryKeys = ["customerId", "productId"],
    foreignKeys = [
        ForeignKey(entity = Customer::class, parentColumns = ["id"], childColumns = ["customerId"]),
        ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"]),
    ],
    indices = [Index("productId")],
)
data class CartEntry(val customerId: Long, val productId: Long, val quantity: Int)

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = Customer::class,
        parentColumns = ["id"],
        childColumns = ["customerId"]
    )],
    indices = [Index("customerId")],
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val orderDate: Long,
    val structure: String,
    val amount: Long,
    val status: String,
)

@Entity(
    tableName = "cheques",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["id"], childColumns = ["orderId"]),
        ForeignKey(entity = Customer::class, parentColumns = ["id"], childColumns = ["customerId"]),
    ],
    indices = [Index("customerId")],
)
data class Cheque(
    @PrimaryKey val orderId: Long,
    val customerId: Long,
    val dateIssued: Long,
    val amount: Long,
    @ColumnInfo(defaultValue = "'онлайн'") val paymentMethod: String = "онлайн",
)
