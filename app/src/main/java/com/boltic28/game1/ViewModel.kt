package com.boltic28.game1

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

class ViewModel(app: Application) : AndroidViewModel(app) {

    private val traffic = mutableListOf<Car>()

    private val lineCenterPositions = mutableListOf<Position>()
    private var currentLine = 0

    private lateinit var car: Car

    private var carWidth = 0f
    private var carHeight = 0f
    private val carHWRelation = 2.05f

    private var viewHeight = 0f
    private var viewWidth = 0f
    private var lineWidth = 0f

    fun initData(viewWidth: Float, viewHeight: Float) {
        this.viewWidth = viewWidth
        this.viewHeight = viewHeight
        lineWidth = viewWidth / LINES_COUNT
        carWidth = lineWidth / 2
        carHeight = carWidth * carHWRelation
        for (i in 0 until LINES_COUNT) {
            lineCenterPositions.add(Position(i * lineWidth + lineWidth / 2, 0f))
        }

        car = Car(
            carImage = getScaledCar(R.drawable.audi),
            line = LINES_COUNT / 2,
            tag = CarTag.MAIN,
            position = Position(
                lineCenterPositions[LINES_COUNT / 2].x - carWidth / 2,
                viewHeight - DISTANCE_TO_CAR - carHeight
            ),
            body = Body(carWidth, carHeight),
            speed = 0f
        )
    }


    fun prepareFrame(): List<Car> {
        return listOf(car)
        //todo move each car on the road
        //            trafficCar.slowDownIfOtherCarInFrontOf()
//            //trafficCar.brokeDownIfContactWithOtherCar()
//            trafficCar.position.y += (car.speed - trafficCar.speed)
//            if (trafficCar.position.y < -trafficCar.body.height || trafficCar.position.y > height) {
//                otherCars.remove(trafficCar)
//            } else {
//                canvas.drawBitmap(trafficCar.carImage, trafficCar.position.x, trafficCar.position.y, paint)
//            }
//        }
    }

    fun turnLeft() {
        var isFinished = false
        CoroutineScope(Dispatchers.Main).launch {
            while (!isFinished) {
                isFinished = car.moveToLeft()
                delay(PAUSE_BETWEEN_DRAWING * 2)
            }
        }
    }

    fun turnRight() {
        var isFinished = false
        CoroutineScope(Dispatchers.Main).launch {
            while (!isFinished) {
                isFinished = car.moveToRight()
                delay(PAUSE_BETWEEN_DRAWING * 2)
            }
        }
    }

    fun accelerate() {
        if (car.speed < MAX_SPEED) car.speed += ACCELERATE_STEP
    }

    fun decelerate() {
        if (car.speed > 0f) car.speed -= ACCELERATE_STEP
    }

    private fun Car.slowDownIfOtherCarInFrontOf(): Boolean {
        traffic.forEach { otherCar ->
            if (this.position.y != otherCar.position.y && this sees otherCar) {
                if (speed >= otherCar.speed) {
                    speed -= ACCELERATE_STEP
                    otherCar.speed += ACCELERATE_STEP
                } else {
                    speed += ACCELERATE_STEP
                }
            }
            return true
        }
        return false
    }

    private fun Car.brokeDownIfContactWithOtherCar(): Boolean {
        traffic.forEach { otherCar ->
            if (this.position.y != otherCar.position.y && this contact otherCar) {
                speed = 0f
                otherCar.speed = 0f
            }
            return true
        }
        return false
    }

    private infix fun Car.contact(car: Car) =
        (this.position.x == car.position.x) && (abs(this.position.y - car.position.y) < MIN_DISTANCE)


    private infix fun Car.sees(car: Car): Boolean =
        (this.position.x == car.position.x) && (abs(this.position.y - car.position.y) < MIN_DISTANCE)


    fun addCar() {
        val localSpeed = getRandomSpeed()
        val line = getRandomLine()
        traffic.add(
            Car(
                carImage = getScaledCar(R.drawable.audi),
                line = line,
                position = Position(
                    lineCenterPositions[line].x - carWidth / 2,
                    if (localSpeed > car.speed) viewHeight else -carHeight
                ),
                body = Body(carWidth, carHeight),
                speed = localSpeed
            )
        )
    }

    private fun getScaledCar(@DrawableRes carImage: Int): Bitmap =
        Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(getApplication<Application>().resources, carImage),
            carWidth.toInt(),
            carHeight.toInt(),
            false
        )

    private fun getRandomLine(): Int {
        var line = Random.nextInt(LINES_COUNT)
        while (line == currentLine) line = Random.nextInt(LINES_COUNT)
        return line
    }

    private fun getRandomSpeed(): Float {
        var lSpeed =
            car.speed + (Random.nextInt(-SPEED_DIFFERENCE, SPEED_DIFFERENCE)) * ACCELERATE_STEP
        while (lSpeed < 0f || lSpeed > MAX_SPEED_OTHER) lSpeed =
            car.speed + (Random.nextInt(-SPEED_DIFFERENCE, SPEED_DIFFERENCE)) * ACCELERATE_STEP
        return lSpeed
    }

    private fun Car.moveToRight(): Boolean =
        if (line + 1 > lineCenterPositions.size - 1) {
            true
        } else {
            position = position.copy(x = position.x + SIDE_STEP_DISTANCE)
            if ((position.x + carWidth / 2) >= lineCenterPositions[line + 1].x) {
                line++
                position = position.copy(x = lineCenterPositions[line].x - carWidth / 2)
                true
            } else {
                false
            }
        }

    private fun Car.moveToLeft(): Boolean =
        if (line - 1 < 0) {
            true
        } else {
            position = position.copy(x = position.x - SIDE_STEP_DISTANCE)
            if ((position.x + carWidth / 2) <= lineCenterPositions[line - 1].x) {
                line--
                position = position.copy(x = lineCenterPositions[line].x - carWidth / 2)
                true
            } else {
                false
            }
        }
}