package com.example.yamakoz.data.local

import androidx.sqlite.db.SupportSQLiteDatabase

private val additionalProductsByCategoryId = mapOf(
    1L to listOf(
        "Холодильник однокамерный" to 21990,
        "Холодильник No Frost" to 56990,
        "Холодильник Side-by-Side" to 89990,
        "Холодильник встраиваемый" to 64990,
    ),
    2L to listOf(
        "Стиральная машина 5 кг" to 24990,
        "Стиральная машина 9 кг" to 42990,
        "Стиральная машина с сушкой" to 59990,
        "Стиральная машина с вертикальной загрузкой" to 39990,
    ),
    3L to listOf(
        "Посудомоечная машина 60 см" to 44990,
        "Посудомоечная машина настольная" to 22990,
        "Посудомоечная машина встраиваемая 45 см" to 41990,
        "Посудомоечная машина встраиваемая 60 см" to 49990,
    ),
    4L to listOf(
        "Пылесос с мешком" to 5990,
        "Пылесос вертикальный" to 12990,
        "Робот-пылесос" to 21990,
        "Пылесос моющий" to 18990,
    ),
    5L to listOf(
        "Микроволновая печь с грилем" to 10990,
        "Микроволновая печь 25 л" to 9990,
        "Микроволновая печь встраиваемая" to 18990,
        "Микроволновая печь инверторная" to 14990,
    ),
    6L to listOf(
        "Электрочайник стеклянный" to 3490,
        "Электрочайник дорожный 0,6 л" to 1690,
        "Электрочайник с терморегулятором" to 5490,
        "Электрочайник стальной 2 л" to 3990,
    ),
    7L to listOf(
        "Кофемашина капсульная" to 9990,
        "Кофемашина рожковая" to 15990,
        "Кофемашина с автокапучинатором" to 65990,
        "Кофемашина встраиваемая" to 94990,
    ),
    8L to listOf(
        "Утюг дорожный" to 1990,
        "Утюг беспроводной" to 5990,
        "Утюг с керамической подошвой" to 3990,
        "Утюг с парогенератором" to 13990,
    ),
    9L to listOf(
        "Мультиварка 3 л" to 4990,
        "Мультиварка 6 л" to 8490,
        "Мультиварка-скороварка" to 10990,
        "Мультиварка с управлением по Wi-Fi" to 12490,
    ),
    10L to listOf(
        "Блендер стационарный" to 5990,
        "Блендер портативный" to 2490,
        "Блендер с измельчителем" to 4990,
        "Блендер вакуумный" to 11990,
    ),
)

internal fun seedAdditionalProducts(db: SupportSQLiteDatabase) {
    db.compileStatement("INSERT INTO products (name, categoryId, price, stock) SELECT ?, ?, ?, 15 WHERE NOT EXISTS (SELECT 1 FROM products WHERE categoryId = ? AND name = ?)").use { insert ->
        for ((categoryId, products) in additionalProductsByCategoryId) {
            for ((name, price) in products) {
                insert.bindString(1, name)
                insert.bindLong(2, categoryId)
                insert.bindLong(3, price * 100L)
                insert.bindLong(4, categoryId)
                insert.bindString(5, name)
                val productId = insert.executeInsert()
                if (productId == -1L) continue
                db.execSQL(
                    "INSERT INTO catalog (productId, categoryId) VALUES (?, ?)",
                    arrayOf<Any?>(productId, categoryId),
                )
            }
        }
    }
}
