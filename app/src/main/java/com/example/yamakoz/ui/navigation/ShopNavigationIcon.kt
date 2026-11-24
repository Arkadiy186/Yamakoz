package com.example.yamakoz.ui.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp

internal enum class ShopTabIcon { Catalog, Cart, Orders }

@Composable
internal fun ShopNavigationIcon(icon: ShopTabIcon, tint: Color) {
    Canvas(Modifier.size(24.dp)) {
        val stroke = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        scale(size.width / 24f, size.height / 24f, pivot = Offset.Zero) {
            when (icon) {
                ShopTabIcon.Catalog -> {
                    for (y in listOf(3f, 13f)) {
                        for (x in listOf(3f, 13f)) {
                            drawRoundRect(tint, Offset(x, y), Size(8f, 8f), CornerRadius(1.5f), style = stroke)
                        }
                    }
                }
                ShopTabIcon.Cart -> {
                    val basket = Path().apply {
                        moveTo(2f, 4f)
                        lineTo(5f, 4f)
                        lineTo(8f, 15f)
                        lineTo(18f, 15f)
                        lineTo(21f, 7f)
                        lineTo(6f, 7f)
                    }
                    drawPath(basket, tint, style = stroke)
                    drawCircle(tint, 1.5f, Offset(9f, 20f))
                    drawCircle(tint, 1.5f, Offset(18f, 20f))
                }
                ShopTabIcon.Orders -> {
                    drawRoundRect(tint, Offset(5f, 3f), Size(14f, 18f), CornerRadius(2f), style = stroke)
                    for (y in listOf(8f, 12f, 16f)) {
                        drawLine(tint, Offset(8.5f, y), Offset(15.5f, y), 1.8f, StrokeCap.Round)
                    }
                }
            }
        }
    }
}
