package com.stars.any_view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.stars.any_view.R

/**
 * Author: Stars
 * Created Date: 2024/11/11
 * Description: 自定义三角形
 */
class TriangleView: View {

    lateinit var paint: Paint
    lateinit var path: Path
    private var triangleType = 1

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0) { init(attrs) }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) { init(attrs) }

    @SuppressLint("Recycle")
    private fun init(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView)
        triangleType = a.getInt(R.styleable.TriangleView_triangle_type, 1)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(a.getColor(R.styleable.TriangleView_bg_color, Color.WHITE))
        paint.style = Paint.Style.FILL
        path = Path()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        path.reset()
        if (triangleType == 2) {
            // 倒三角
            path.lineTo(width / 2f, height.toFloat())
            path.lineTo(width.toFloat(), 0f)
        }else {
            // 正三角
            path.moveTo(width / 2f, 0f)
            path.lineTo(0f, height.toFloat())
            path.lineTo(width.toFloat(), height.toFloat())
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}