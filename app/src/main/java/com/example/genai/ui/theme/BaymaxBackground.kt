package com.example.genai.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BaymaxBackground = Builder(
    name = "BaymaxBackground",
    defaultWidth = 100.dp,
    defaultHeight = 100.dp,
    viewportWidth = 100f,
    viewportHeight = 100f
).apply {
    path(fill = SolidColor(Color(0x0A000000)), stroke = null, strokeLineWidth = 0.0f) {
        moveTo(10f, 10f)
        lineTo(20f, 10f)
        lineTo(20f, 20f)
        lineTo(10f, 20f)
        close()
        moveTo(80f, 80f)
        lineTo(90f, 80f)
        lineTo(90f, 90f)
        lineTo(80f, 90f)
        close()
    }
}.build()