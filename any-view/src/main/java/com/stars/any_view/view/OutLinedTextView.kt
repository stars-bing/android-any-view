package com.stars.any_view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.stars.any_view.R


/**
 * Author: Stars
 * Created Date: 2024/8/16
 * Description: 字体-描边
 */
class OutLinedTextView: AppCompatTextView {

    // 描边宽度
    private var fontStroke = 0f
    // 描边颜色
    private var fontStrokeColor = 0xFF000000.toInt()

    constructor(context: Context): super(context)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.OutLinedTextView)
        fontStroke = a.getDimension(R.styleable.OutLinedTextView_font_stroke, 0f)
        fontStrokeColor = a.getColor(R.styleable.OutLinedTextView_font_stroke_color, fontStrokeColor)
    }

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.OutLinedTextView)
        fontStroke = a.getDimension(R.styleable.OutLinedTextView_font_stroke, 0f)
        fontStrokeColor = a.getColor(R.styleable.OutLinedTextView_font_stroke_color, fontStrokeColor)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val text = text.toString()
        // 保存当前文本画笔的状态
        val currentTextColor = currentTextColor

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = fontStroke
        paint.color = fontStrokeColor
        canvas.drawText(text, (width - paint.measureText(text)) / 2, baseline.toFloat(), paint)

        // 恢复原本的文本画笔
        paint.style = Paint.Style.FILL
        paint.setColor(currentTextColor)
        canvas.drawText(text, (width - paint.measureText(text)) / 2, baseline.toFloat(), paint)
//        super.onDraw(canvas)
    }

}