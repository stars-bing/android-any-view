package com.stars.any_view.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.stars.any_view.R

/**
 * Author：Stars
 * Date：2024/7/6 15:08
 * Describe：图片：圆形、圆角
 **/
class RoundImageView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    // 图片的类型，圆形or圆角
    private var type: Int = 0

    // 描边的颜色、宽度
    private var mBorderColor: Int
    private var mBorderWidth: Float

    // 圆角的大小
    private var mCornerRadius: Float

    //左上角圆角大小
    private var mLeftTopCornerRadius: Float

    //右上角圆角大小
    private var mRightTopCornerRadius: Float

    //左下角圆角大小
    private var mLeftBottomCornerRadius: Float

    //右下角圆角大小
    private var mRightBottomCornerRadius: Float

    /**
     * 绘图的Paint
     */
    private var mBitmapPaint: Paint? = null
    private var mBorderPaint: Paint? = null

    // 圆角的半径
    private var mRadius = 0f

    // 3x3 矩阵，主要用于缩小放大
    private var mMatrix: Matrix? = null

    // 渲染图像，使用图像为绘制图形着色
    private var mBitmapShader: BitmapShader? = null

    // view的宽度
    private var mWidth = 0

    // 圆角图片区域
    private var mRoundRect: RectF? = null
    private var mRoundPath: Path? = null
    var isUseUnitDip = true

   init {
       val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0)
       type = a.getInt(R.styleable.RoundImageView_type, TYPE_OVAL)
       mBorderColor = a.getColor(R.styleable.RoundImageView_border_color, Color.WHITE)
       mBorderWidth = a.getDimension(R.styleable.RoundImageView_border_width, 0f)
       mCornerRadius =
           a.getDimension(R.styleable.RoundImageView_corner_radius, dp2px(10f).toFloat())
       mLeftTopCornerRadius = a.getDimension(R.styleable.RoundImageView_leftTop_corner_radius, 0f)
       mLeftBottomCornerRadius =
           a.getDimension(R.styleable.RoundImageView_leftBottom_corner_radius, 0f)
       mRightTopCornerRadius =
           a.getDimension(R.styleable.RoundImageView_rightTop_corner_radius, 0f)
       mRightBottomCornerRadius =
           a.getDimension(R.styleable.RoundImageView_rightBottom_corner_radius, 0f)
       a.recycle()
       init()
   }

    private fun init() {
        mRoundPath = Path()
        mRoundRect = RectF()
        mMatrix = Matrix()
        mBitmapPaint = Paint()
        mBitmapPaint!!.setAntiAlias(true)
        mBorderPaint = Paint()
        mBorderPaint!!.setAntiAlias(true)
        mBorderPaint!!.setStyle(Paint.Style.STROKE)
        updateStrokePaint()
    }

    private fun updateStrokePaint() {
        mBorderPaint!!.setColor(mBorderColor)
        mBorderPaint!!.setStrokeWidth(mBorderWidth)
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec)
            )
            mRadius = mWidth / 2 - mBorderWidth / 2
            setMeasuredDimension(mWidth, mWidth)
        }
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 圆角图片的范围
        if (type == TYPE_ROUND || type == TYPE_OVAL) {
            mRoundRect!!.set(
                mBorderWidth / 2,
                mBorderWidth / 2,
                w - mBorderWidth / 2,
                h - mBorderWidth / 2
            )
        }
    }

    protected override fun onDraw(canvas: Canvas) {
        if (getDrawable() == null) {
            return
        }
        setupShader()
        if (type == TYPE_ROUND) {
            setRoundPath()
            canvas.drawPath(mRoundPath!!, mBitmapPaint!!)

            //绘制描边
            if (mBorderWidth > 0) {
                canvas.drawPath(mRoundPath!!, mBorderPaint!!)
            }
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(
                mRadius + mBorderWidth / 2,
                mRadius + mBorderWidth / 2,
                mRadius,
                mBitmapPaint!!
            )

            //绘制描边
            if (mBorderWidth > 0) {
                canvas.drawCircle(
                    mRadius + mBorderWidth / 2,
                    mRadius + mBorderWidth / 2,
                    mRadius,
                    mBorderPaint!!
                )
            }
        } else {
            canvas.drawOval(mRoundRect!!, mBitmapPaint!!)

            //绘制描边
            if (mBorderWidth > 0) {
                canvas.drawOval(mRoundRect!!, mBorderPaint!!)
            }
        }
    }

    private fun setRoundPath() {
        mRoundPath!!.reset()
        /**
         * 如果四个圆角大小都是默认值0，
         * 则将四个圆角大小设置为mCornerRadius的值
         */
        if (mLeftTopCornerRadius == 0f && mLeftBottomCornerRadius == 0f && mRightTopCornerRadius == 0f && mRightBottomCornerRadius == 0f) {
            mRoundPath!!.addRoundRect(
                mRoundRect!!, floatArrayOf(
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius,
                    mCornerRadius, mCornerRadius
                ),
                Path.Direction.CW
            )
        } else {
            mRoundPath!!.addRoundRect(
                mRoundRect!!, floatArrayOf(
                    mLeftTopCornerRadius, mLeftTopCornerRadius,
                    mRightTopCornerRadius, mRightTopCornerRadius,
                    mRightBottomCornerRadius, mRightBottomCornerRadius,
                    mLeftBottomCornerRadius, mLeftBottomCornerRadius
                ),
                Path.Direction.CW
            )
        }
    }

    /**
     * 初始化BitmapShader
     */
    private fun setupShader() {
        val drawable: Drawable = getDrawable() ?: return
        val bmp: Bitmap = drawableToBitmap(drawable) ?: return
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mMatrix!!.setTranslate(0f, 0f)
        var scale = 1.0f
        if (type == TYPE_CIRCLE) {
            if (bmp.width != width || bmp.height != height) {
                // 拿到bitmap宽或高的小值
                val bSize: Int = Math.min(bmp.width, bmp.height)
                scale = mWidth * 1.0f / bSize
                //使缩放后的图片居中
                val dx: Float = (bmp.getWidth() * scale - mWidth) / 2
                val dy: Float = (bmp.getHeight() * scale - mWidth) / 2
                mMatrix!!.setTranslate(-dx, -dy)
            }
        } else if (type == TYPE_ROUND || type == TYPE_OVAL) {
            if (bmp.getWidth() != getWidth() || bmp.getHeight() != getHeight()) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(
                    getWidth() * 1.0f / bmp.getWidth(),
                    getHeight() * 1.0f / bmp.getHeight()
                )
                //使缩放后的图片居中
                val dx: Float = (scale * bmp.getWidth() - getWidth()) / 2
                val dy: Float = (scale * bmp.getHeight() - getHeight()) / 2
                mMatrix!!.setTranslate(-dx, -dy)
            }
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix!!.preScale(scale, scale)
        // 设置变换矩阵
        mBitmapShader!!.setLocalMatrix(mMatrix)
        // 设置shader
        mBitmapPaint!!.shader = mBitmapShader
    }

    /**
     * drawable转bitmap
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            val bd: BitmapDrawable = drawable as BitmapDrawable
            return bd.getBitmap()
        }
        val w: Int = drawable.getIntrinsicWidth()
        val h: Int = drawable.getIntrinsicHeight()
        var bitmap: Bitmap? = null
        try {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(canvas)
        } catch (ignore: OutOfMemoryError) {
        }
        return bitmap
    }

    /**
     * 设置图片类型:
     * imageType=0 圆形图片
     * imageType=1 圆角图片
     * 默认为圆形图片
     */
    fun setType(imageType: Int): RoundImageView {
        if (type != imageType) {
            type = imageType
            if (type != TYPE_ROUND && type != TYPE_CIRCLE && type != TYPE_OVAL) {
                type = TYPE_OVAL
            }
            requestLayout()
        }
        return this
    }

    /**
     * 设置圆角图片的圆角大小
     */
    fun setCornerRadius(cornerRadius: Int): RoundImageView {
        var cornerRadius = cornerRadius
        cornerRadius = dp2px(cornerRadius.toFloat())
        if (mCornerRadius != cornerRadius.toFloat()) {
            mCornerRadius = cornerRadius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的左上圆角大小
     */
    fun setLeftTopCornerRadius(cornerRadius: Int): RoundImageView {
        var cornerRadius = cornerRadius
        cornerRadius = dp2px(cornerRadius.toFloat())
        if (mLeftTopCornerRadius != cornerRadius.toFloat()) {
            mLeftTopCornerRadius = cornerRadius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的右上圆角大小
     */
    fun setRightTopCornerRadius(cornerRadius: Int): RoundImageView {
        var cornerRadius = cornerRadius
        cornerRadius = dp2px(cornerRadius.toFloat())
        if (mRightTopCornerRadius != cornerRadius.toFloat()) {
            mRightTopCornerRadius = cornerRadius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的左下圆角大小
     */
    fun setLeftBottomCornerRadius(cornerRadius: Int): RoundImageView {
        var cornerRadius = cornerRadius
        cornerRadius = dp2px(cornerRadius.toFloat())
        if (mLeftBottomCornerRadius != cornerRadius.toFloat()) {
            mLeftBottomCornerRadius = cornerRadius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置圆角图片的右下圆角大小
     */
    fun setRightBottomCornerRadius(cornerRadius: Int): RoundImageView {
        var cornerRadius = cornerRadius
        cornerRadius = dp2px(cornerRadius.toFloat())
        if (mRightBottomCornerRadius != cornerRadius.toFloat()) {
            mRightBottomCornerRadius = cornerRadius.toFloat()
            invalidate()
        }
        return this
    }

    /**
     * 设置描边宽度
     */
    fun setBorderWidth(borderWidth: Int): RoundImageView {
        var borderWidth = borderWidth
        borderWidth = dp2px(borderWidth.toFloat())
        if (mBorderWidth != borderWidth.toFloat()) {
            mBorderWidth = borderWidth.toFloat()
            updateStrokePaint()
            invalidate()
        }
        return this
    }

    /**
     * 设置描边颜色
     */
    fun setBorderColor(borderColor: Int): RoundImageView {
        if (mBorderColor != borderColor) {
            mBorderColor = borderColor
            updateStrokePaint()
            invalidate()
        }
        return this
    }


    companion object {
        const val TYPE_CIRCLE = 0
        const val TYPE_ROUND = 1
        const val TYPE_OVAL = 2
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}