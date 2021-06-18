package com.boltic28.game1

import android.os.PowerManager

const val START_COORDINATE = 0f

const val LINE_WIDTH = 15.0f
const val LINE_LENGTH = 100.0f
const val SPACE_BETWEEN_LINES = LINE_LENGTH
const val LINES_COUNT = 5
const val PAUSE_BETWEEN_DRAWING = 16L
const val ACCELERATE_STEP = LINE_LENGTH / 20
const val SIDE_STEP_DISTANCE = LINE_LENGTH / 5
const val MAX_SPEED = LINE_LENGTH
const val SPEEDOMETER_CORRECTION = 2.5f // max speed 100 * 2.5f
const val DISTANCE_TO_CAR = 40f

// traffic
const val CAR_APPEARANCE_TIME = 2000L
const val MAX_SPEED_OTHER = LINE_LENGTH - ACCELERATE_STEP * 4
const val MIN_DISTANCE = 500f
const val SPEED_DIFFERENCE = 8 // in Accelerate steps

// board
const val TEXT_SIZE = 26.0f

// cars
const val MAIN = "main"
const val OTHER = "other"