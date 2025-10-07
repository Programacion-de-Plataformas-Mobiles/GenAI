package com.example.genai.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BaymaxLogo = Builder(
    name = "BaymaxLogo",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 100f,
    viewportHeight = 100f
).apply {
    path(
        fill = SolidColor(Color.Black),
        stroke = null,
        strokeLineWidth = 0.0f,
        strokeLineCap = Butt,
        strokeLineJoin = Miter,
        strokeLineMiter = 4.0f,
        pathFillType = NonZero
    ) {
        moveTo(35.0f, 45.0f)
        arcToRelative(5.0f, 5.0f, 0.0f, true, true, -10.0f, 0.0f)
        arcToRelative(5.0f, 5.0f, 0.0f, true, true, 10.0f, 0.0f)
        close()
        moveTo(75.0f, 45.0f)
        arcToRelative(5.0f, 5.0f, 0.0f, true, true, -10.0f, 0.0f)
        arcToRelative(5.0f, 5.0f, 0.0f, true, true, 10.0f, 0.0f)
        close()
        moveTo(40.0f, 50.0f)
        horizontalLineToRelative(20.0f)
        verticalLineToRelative(2.0f)
        horizontalLineToRelative(-20.0f)
        close()
    }
}.build()