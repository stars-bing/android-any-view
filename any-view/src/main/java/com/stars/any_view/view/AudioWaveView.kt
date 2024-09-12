package com.stars.any_view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.stars.any_view.R


/**
 * Author: Stars
 * Created Date: 2024/7/8
 * Description: 自定义格条(如语音波浪进度条)
 */
class AudioWaveView: View {

    var barHeight = 30f
    var barWidth = 3f * 2.5f
    var spaceWidth = 3f * 2.5f
    var wProgress = 0

    /**
     * 进度颜色
     */
    var wProgressColor = context.getColor(R.color.black)

    /**
     * 默认(背景)颜色
     */
    var defaultColor = context.getColor(R.color.white)
    var wMaxProgress = 5
    var waveSizeList = arrayListOf<Int>()

    var paintBar: Paint? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        paintBar = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBar?.setColor(defaultColor)
        paintBar?.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(((barWidth + spaceWidth) * waveSizeList.size).toInt(), heightMeasureSpec)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until  waveSizeList.size) {
            val startX = barWidth * (i+1) + spaceWidth * (i+1) // 条形起始X坐标
            // 条形起始Y坐标（留出部分空间以便显示）
//            if (i % 3 == 0) {
//                barHeight += 16f
//            }else if (i % 5 == 0) {
//                barHeight = 16f
//            }else {
//                barHeight = 30f
//            }
            barHeight = waveSizeList[i].toFloat() * 2.5f
            if (i+1 > wProgress) {
                paintBar?.color = defaultColor
            }else {
                paintBar?.color = wProgressColor
            }
//            val startY = (measuredHeight - barHeight) / 2
            val startY = measuredHeight - barHeight
            // 绘制条形
            val rect = RectF(startX, startY, startX + barWidth, startY + barHeight)
            canvas.drawRoundRect(rect, 4f, 4f,  paintBar!!)
        }
    }

    // 设置线条颜色
    fun setWaveColor(color: Int) {
        defaultColor = color
        setMeasuredDimension(((barWidth + spaceWidth) * wMaxProgress).toInt(), measuredHeight)
        invalidate()
        requestLayout()
    }

    // 设置进度
    fun setWaveProgress(progress: Int) {
        this.wProgress = progress
        setMeasuredDimension(((barWidth + spaceWidth) * waveSizeList.size).toInt(), measuredHeight)
        invalidate()
        requestLayout()
    }

    // 设置最大进度
    fun setMaxWaveProgress(max: Int) {
        this.wMaxProgress = max
        setMeasuredDimension(((barWidth + spaceWidth) * wMaxProgress).toInt(), measuredHeight)
        invalidate()
        requestLayout()
    }

    fun setMaxWaveList(data: ArrayList<Int>) {
        this.waveSizeList = data
        this.wMaxProgress = data.size
        setMeasuredDimension(((barWidth + spaceWidth) * waveSizeList.size).toInt(), measuredHeight)
        invalidate()
        requestLayout()
    }

    // 设置进度颜色
    fun setWaveProgressColor(color: Int) {
        this.wProgressColor = color
        setMeasuredDimension(((barWidth + spaceWidth) * wMaxProgress).toInt(), measuredHeight)
        invalidate()
        requestLayout()
    }

}