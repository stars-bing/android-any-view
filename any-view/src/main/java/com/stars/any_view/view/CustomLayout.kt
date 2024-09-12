package com.stars.any_view.view

import android.content.Context
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.max


/**
 * Author: Stars
 * Created Date: 2024/7/8
 * Description: 自定义布局（第一个子View必须为：TextView）：View可在TextView后面拼接起来
 * 使用情景：一般第一个为TextView，第二个为任意布局。作用：让第二个布局跟在TextView后面
 */
class CustomLayout: ViewGroup {

    //单行显示
    private val SINGLE_LINE = 0x01

    //多行显示
    private val MULTI_LINE = 0x02

    //显示到下一行
    private val NEXT_LINE = 0x03

    //显示样式
    private var type = 0

    //绘制文字最后一行的顶部坐标
    private var lastLineTop = 0

    //绘制文字最后一行的右边坐标
    private var lastLineRight = 0f

    constructor(context: Context?):super(context)

    constructor(context: Context?, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = childCount
        val w = MeasureSpec.getSize(widthMeasureSpec)
        if (childCount == 2) {
            var tv: TextView? = null
            if (getChildAt(0) is TextView) {
                tv = getChildAt(0) as TextView
                initTextParams(tv.getText(), tv!!.measuredWidth, tv.paint)
            } else {
                throw RuntimeException("CustomLayout first child view not a TextView")
            }
            val sencodView = getChildAt(1)

            //测量子view的宽高
            measureChildren(widthMeasureSpec, heightMeasureSpec)

            //两个子view宽度相加小于该控件宽度的时候
            if (tv!!.measuredWidth + sencodView.measuredWidth <= w) {
                val width = tv.measuredWidth + sencodView.measuredWidth
                //计算高度
                val height = max(tv.measuredHeight.toDouble(), sencodView.measuredHeight.toDouble()).toInt()
                //设置该viewgroup的宽高
                setMeasuredDimension(width, height)
                type = SINGLE_LINE
                return
            }

            if (getChildAt(0) is TextView) {
                //最后一行文字的宽度加上第二个view的宽度大于viewgroup宽度时第二个控件换行显示
                if (lastLineRight + sencodView.measuredWidth > w) {
                    setMeasuredDimension(tv.measuredWidth, tv.measuredHeight + sencodView.measuredHeight)
                    type = NEXT_LINE
                    return
                }
                val height = max(tv.measuredHeight.toDouble(), (lastLineTop + sencodView.measuredHeight).toDouble())
                    .toInt()
                setMeasuredDimension(tv.measuredWidth, height)
                type = MULTI_LINE
            }
        } else {
            throw RuntimeException("CustomLayout child count must is 2")
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (type == SINGLE_LINE || type == MULTI_LINE) {
            val tv = getChildAt(0) as TextView
            val v1 = getChildAt(1)
            //设置第二个view在Textview文字末尾位置
            tv.layout(0, 0, tv.measuredWidth, tv.measuredHeight)
            val left = lastLineRight.toInt()
            var top = lastLineTop
            //最后一行的高度 注:通过staticLayout得到的行高不准确故采用这种方式
            val lastLineHeight = tv.bottom - tv.paddingBottom - lastLineTop
            //当第二view高度小于单行文字高度时竖直居中显示
            if (v1.measuredHeight < lastLineHeight) {
                top = lastLineTop + (lastLineHeight - v1.measuredHeight) / 2
            }
            if (type == SINGLE_LINE) {
                v1.layout(width - v1.measuredWidth, top, width, top + v1.measuredHeight)
            }else {
                v1.layout(tv.measuredWidth - v1.measuredWidth, height - v1.measuredHeight, tv.measuredWidth, height)
            }
//            LogUtils.a(tv.text, type, measuredWidth, tv.measuredWidth, v1.measuredWidth, width, top)
        } else if (type == NEXT_LINE) {
            val v0 = getChildAt(0) as TextView
            val v1 = getChildAt(1)
            //设置第二个view换行显示
            v0.layout(0, 0, v0.measuredWidth, v0.measuredHeight)
            if (v0.measuredWidth + v1.measuredWidth > width) {
                v1.layout(width - v1.measuredWidth, v0.measuredHeight, width, v0.measuredHeight + v1.measuredHeight)
            }else {
                v1.layout(v0.measuredWidth, v0.measuredHeight, v0.measuredWidth + v1.measuredWidth, v0.measuredHeight + v1.measuredHeight)
            }
        }
    }

    /**
     * 得到Textview绘制文字的基本信息
     * @param text Textview的文字内容
     * @param maxWidth Textview的宽度
     * @param paint 绘制文字的paint
     */
    private fun initTextParams(text: CharSequence, maxWidth: Int, paint: TextPaint) {
        val staticLayout = StaticLayout(text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
        val lineCount = staticLayout.lineCount
        lastLineTop = staticLayout.getLineTop(lineCount - 1)
        lastLineRight = staticLayout.getLineRight(lineCount - 1)
    }

}