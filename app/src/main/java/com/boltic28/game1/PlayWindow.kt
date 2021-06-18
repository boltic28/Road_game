package com.boltic28.game1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class PlayWindow : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val paint = Paint()
    private var speed = 0f
    private var moveCorrection = 0f
    private var shortLine = 0f

    private var traffic = emptyList<Car>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(ContextCompat.getColor(context, R.color.road))

        canvas?.let {
            drawLines(it)
            drawTraffic(it)
        }
    }

    fun updateState(traffic: List<Car>): Float {
        this.traffic = traffic
        speed = traffic.first { it.tag == CarTag.MAIN }.speed
        calculateLines()
        invalidate()
        return speed * SPEEDOMETER_CORRECTION
    }

    private fun drawTraffic(canvas: Canvas) {
        traffic.onEach { trafficCar ->
            canvas.drawBitmap(
                trafficCar.carImage,
                trafficCar.position.x,
                trafficCar.position.y,
                paint
            )
        }
    }

    private fun drawLines(canvas: Canvas) {
        paint.lines()
        val lineWidth = this.width / LINES_COUNT.toFloat()
        val lineParts = (this.height / (LINE_LENGTH * 2)).toInt()

        for (i in 1 until LINES_COUNT) {
            var y = if (isShortLine()) START_COORDINATE else START_COORDINATE + moveCorrection
            for (k in 0..lineParts) {
                if (isShortLine() && k == 0) {
                    canvas.drawLine(i * lineWidth, y, i * lineWidth, y + shortLine, paint)
                } else {
                    canvas.drawLine(i * lineWidth, y, i * lineWidth, y + LINE_LENGTH, paint)
                }
                y += if (isShortLine() && k == 0) shortLine + SPACE_BETWEEN_LINES else LINE_LENGTH + SPACE_BETWEEN_LINES
            }
        }
    }

    private fun isShortLine(): Boolean = shortLine > 0f

    private fun calculateLines() {
        moveCorrection += speed
        if (moveCorrection > LINE_LENGTH) {
            shortLine = moveCorrection - LINE_LENGTH
        }
        if (moveCorrection >= LINE_LENGTH + SPACE_BETWEEN_LINES) {
            moveCorrection = 0f
            shortLine = 0f
        }
    }

    private fun Paint.lines(): Paint {
        color = ContextCompat.getColor(context, R.color.lines)
        strokeWidth = LINE_WIDTH
        return this
    }

    private fun Paint.car(): Paint {
        color = ContextCompat.getColor(context, R.color.car)
        //strokeWidth = stepX * Constants.GRILL_SCALE_TO_SECTOR
        return this
    }

    private fun Paint.barrier(): Paint {
        color = ContextCompat.getColor(context, R.color.barrier)
        return this
    }

    private fun Paint.text(): Paint {
        color = ContextCompat.getColor(context, R.color.text)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = TEXT_SIZE
        return this
    }
}