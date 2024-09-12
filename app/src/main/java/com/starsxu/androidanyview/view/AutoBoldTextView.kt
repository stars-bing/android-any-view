package com.starsxu.androidanyview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.starsxu.androidanyview.R

/**
 * Author: Stars
 * Created Date: 2024/8/9
 * Description: 自定义字体的粗细大小(用描边效果来实现)
 */
class AutoBoldTextView: AppCompatTextView {

    private var boldSize = NORMAL

    companion object {
        const val NORMAL = 1f
        const val MIDDLE = 1.6f
        const val LARGE = 2.4f
        const val BIGGER = 4.6f
    }

    constructor(context: Context): super(context)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AutoBoldTextView)
        initSize(a.getInt(R.styleable.AutoBoldTextView_font_size, 1))
    }

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AutoBoldTextView)
        initSize(a.getInt(R.styleable.AutoBoldTextView_font_size, 1))
    }

    private fun initSize(sizeType: Int) {
        boldSize = when(sizeType) {
            1 -> NORMAL
            2 -> MIDDLE
            3 -> LARGE
            4 -> BIGGER
            else -> NORMAL
        }
    }

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.strokeWidth = boldSize
        super.draw(canvas)
    }

}