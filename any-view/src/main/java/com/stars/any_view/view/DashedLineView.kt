package com.stars.any_view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.stars.any_view.R


/**
 * Author: Stars
 * Created Date: 2025/1/22
 * Description: 自定义虚线
 */
class DashedLineView: View {

    lateinit var paint: Paint
    private var dashLength = 20f
    private var gapLength = 10f
    // 默认为横向虚线 1横向 2竖向
    private var lineType = 1
    private var lineSize = 5f

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { init(attrs) }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {init(attrs)}

    @SuppressLint("Recycle")
    private fun init(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DashedLineView)
        lineType = a.getInt(R.styleable.DashedLineView_line_type, 1)
        dashLength = a.getDimension(R.styleable.DashedLineView_dash_length, 10f)
        gapLength = a.getDimension(R.styleable.DashedLineView_gap_length, 5f)
        lineSize = a.getDimension(R.styleable.DashedLineView_line_size, 5f)
        paint = Paint()
        paint.color = a.getColor(R.styleable.DashedLineView_line_color, Color.BLACK)
        paint.strokeWidth = lineSize
        paint.style = Paint.Style.STROKE
        paint.setPathEffect(DashPathEffect(floatArrayOf(dashLength, gapLength), 0f))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (lineType == 1) {
            canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, paint)
        }else {
            canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), paint)
        }
    }
}