package com.example.yamakoz.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yamakoz.data.local.entity.*

@Database(
    entities = [Category::class, Product::class, CatalogEntry::class, Customer::class, CartEntry::class, Order::class, Cheque::class],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "shop.db")
                .fallbackToDestructiveMigrationFrom(1, 2, 3)
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        // Room calls onOpen after both initial creation and a destructive schema reset.
                        db.beginTransaction()
                        try {
                            val needsSeed = db.query("SELECT 1 FROM categories LIMIT 1").use { !it.moveToFirst() }
                            if (needsSeed) {
                                installTriggers(db)
                                seed(db)
                                seedAdditionalProducts(db)
                            }
                            db.setTransactionSuccessful()
                        } finally {
                            db.endTransaction()
                        }
                    }
                })
                .build()
    }
}

private fun installTriggers(db: SupportSQLiteDatabase) {
    val checks = listOf(
        Triple("products", "invalid_stock", "NEW.stock < 0"),
        Triple("cart", "invalid_quantity", "NEW.quantity <= 0"),
        Triple("orders", "invalid_status", "NEW.status NOT IN ('awaiting_payment', 'paid', 'cancelled')"),
        Triple("cheques", "invalid_cheque", "NOT EXISTS (SELECT 1 FROM orders WHERE id = NEW.orderId AND customerId = NEW.customerId AND status = 'paid' AND amount = NEW.amount)"),
    )
    for ((table, name, condition) in checks) {
        for (operation in listOf("INSERT", "UPDATE")) {
            db.execSQL("""
                CREATE TRIGGER IF NOT EXISTS ${name}_${operation} BEFORE $operation ON $table
                WHEN $condition BEGIN SELECT RAISE(ABORT, '$name'); END
            """.trimIndent())
        }
    }
    db.execSQL("""
        CREATE TRIGGER IF NOT EXISTS preserve_paid_order BEFORE UPDATE ON orders
        WHEN EXISTS (SELECT 1 FROM cheques WHERE orderId = OLD.id)
            AND (NEW.status <> 'paid' OR NEW.customerId <> OLD.customerId OR NEW.amount <> OLD.amount)
        BEGIN SELECT RAISE(ABORT, 'invalid_paid_order'); END
    """.trimIndent())
}

private fun seed(db: SupportSQLiteDatabase) {
    val inventory = listOf(
        "Холодильники" to "Холодильник двухкамерный",
        "Стиральные машины" to "Стиральная машина 7 кг",
        "Посудомоечные машины" to "Посудомоечная машина 45 см",
        "Пылесосы" to "Пылесос с контейнером",
        "Микроволновые печи" to "Микроволновая печь 20 л",
        "Чайники" to "Электрочайник 1,7 л",
        "Кофемашины" to "Кофемашина автоматическая",
        "Утюги" to "Утюг с паром",
        "Мультиварки" to "Мультиварка 5 л",
        "Блендеры" to "Блендер погружной",
    )
    val prices = listOf(45990, 32990, 37990, 8990, 7490, 2990, 48990, 4790, 6990, 3990)
    inventory.forEachIndexed { index, (category, product) ->
        val id = index + 1L
        val price = prices[index] * 100L
        db.execSQL("INSERT INTO categories (id, name) VALUES (?, ?)", arrayOf<Any?>(id, category))
        db.execSQL("INSERT INTO products (id, name, categoryId, price, stock) VALUES (?, ?, ?, ?, 15)", arrayOf<Any?>(id, product, id, price))
        db.execSQL("INSERT INTO catalog (productId, categoryId) VALUES (?, ?)", arrayOf<Any?>(id, id))
    }
}
