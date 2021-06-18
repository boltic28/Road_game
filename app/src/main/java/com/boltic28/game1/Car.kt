package com.boltic28.game1

import android.graphics.Bitmap

data class Car (
    val tag: CarTag = CarTag.OTHER,
    val carImage: Bitmap,
    var line: Int,
    var position: Position = Position(0f,0f),
    val body: Body = Body(0f,0f),
    var speed: Float = 0.0f,
    var rotation: Float = 0.0f
)