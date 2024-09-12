package com.starsxu.androidanyview.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.starsxu.androidanyview.R


/**
 * Author: Stars
 * Created Date: 2024/5/20
 * Description: 圆环进度度
 */
class RingProgressBar: View {

    private lateinit var paint: Paint
    private var rect: RectF? = null
    private var progress = 0
    private var maxProgress = 100
    private var radius = 100
    private var strokeWidth = 10f
    var strokeColor: Int = Color.LTGRAY
    var progressColor: Int = Color.BLUE

    constructor(context: Context): super(context) {
        init()
    }

    @SuppressLint("CustomViewStyleable", "Recycle")
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ringProgressBar)
        strokeColor = attr.getColor(R.styleable.ringProgressBar_strokeColor, Color.LTGRAY)
        progressColor = attr.getColor(R.styleable.ringProgressBar_progressColor, Color.BLUE)
        strokeWidth = attr.getDimension(R.styleable.ringProgressBar_strokeWidth, 10f)
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, 100)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(100, heightMeasureSpec)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpec, 100)
        }
    }

    private fun init() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        rect = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2
        val centerY = height / 2
        radius = ((width / 2) - strokeWidth).toInt()


        // Draw the background circle
        paint.setColor(strokeColor)
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)

        // Draw the progress arc
        paint.setColor(progressColor)
        canvas.drawArc(strokeWidth, strokeWidth, (centerX + radius).toFloat(),
            (centerY + radius).toFloat(),-90f, progress.toFloat() / maxProgress * 360,false, paint);
//        canvas.drawArc(rect!!, -90f, progress.toFloat() / maxProgress * 360, false, paint)
    }

    fun setProgress(progress: Int) {

        val animator = ValueAnimator.ofFloat(0f, 100f)
        animator.addUpdateListener { animation ->
            this.progress = progress
            invalidate() //刷新 View
        }
        animator.setDuration(50)
        animator.interpolator = AccelerateInterpolator()
        animator.start()
    }

    fun getCurrentProgress(): Int {
        return progress
    }

    fun setMaxProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
    }

    fun setStrokeWidth(float: Float) {
        this.strokeWidth = float
        invalidate()
    }

}