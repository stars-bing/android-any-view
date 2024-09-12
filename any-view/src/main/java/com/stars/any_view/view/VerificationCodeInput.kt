package com.stars.any_view.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import com.stars.any_view.R

/**
 * Author: Stars
 * Created Date: 2024/9/12
 * Description:
 */
@SuppressLint("Recycle")
class VerificationCodeInput(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs), TextWatcher, View.OnKeyListener {
    private var box = 4
    private var boxWidth = 80
    private var boxHeight = 80
    private var childHPadding = 14
    private var childVPadding = 14
    private var inputType: String? = TYPE_NUMBER
    private var textColor = context.getColor(R.color.black)

    // 默认背景
    private var boxBgNormal: Drawable? = null

    // 游标背景
    private var boxSelectBg: Drawable? = null

    // 输入后背景
    private var boxInputEdBg: Drawable? = null
    private var listener: Listener? = null
    private val mEditTextList: MutableList<EditText?>? = ArrayList()
    private var currentPosition = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInput)
        box = a.getInt(R.styleable.VerificationCodeInput_box, 4)

        childHPadding = a.getDimension(R.styleable.VerificationCodeInput_child_h_padding, 0f).toInt()
        childVPadding = a.getDimension(R.styleable.VerificationCodeInput_child_v_padding, 0f).toInt()
        boxBgNormal = a.getDrawable(R.styleable.VerificationCodeInput_box_bg)
        boxSelectBg = a.getDrawable(R.styleable.VerificationCodeInput_box_select_bg)
        boxInputEdBg = a.getDrawable(R.styleable.VerificationCodeInput_box_input_ed_bg)
        inputType = a.getString(R.styleable.VerificationCodeInput_inputType)
        boxWidth = a.getDimension(R.styleable.VerificationCodeInput_child_width, boxWidth.toFloat()).toInt()
        boxHeight = a.getDimension(R.styleable.VerificationCodeInput_child_height, boxHeight.toFloat()).toInt()
        textColor = a.getColor(R.styleable.VerificationCodeInput_inputTextColor, textColor)
        initViews()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initViews() {
        for (i in 0 until box) {
            val editText = EditText(context)
            val layoutParams = LayoutParams(boxWidth, boxHeight)
            layoutParams.bottomMargin = childVPadding
            layoutParams.topMargin = childVPadding
            layoutParams.leftMargin = childHPadding
            layoutParams.rightMargin = childHPadding
            layoutParams.gravity = Gravity.CENTER

            editText.setOnKeyListener(this)

            if (boxBgNormal == null) {
                boxBgNormal = context.getDrawable(R.drawable.verification_edit_bg_focus)
            }

            if (i == 0) {
                editText.isEnabled = true
                editText.requestFocus()
                if (boxSelectBg != null) {
                    editText.background = boxSelectBg
                } else {
                    editText.background = boxBgNormal
                }
            } else {
                editText.isEnabled = false
                editText.clearFocus()
                editText.background = boxBgNormal
            }
            editText.setTextColor(textColor)
            editText.textSize = 24f
            editText.maxLines = 1
            editText.setTypeface(Typeface.MONOSPACE) //din字体
            editText.layoutParams = layoutParams
            editText.gravity = Gravity.CENTER
            editText.setPadding(0, 0, 0, 0)
            editText.filters = arrayOf<InputFilter>(LengthFilter(1))

            if (TYPE_NUMBER == inputType) {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
            } else if (TYPE_PASSWORD == inputType) {
                editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                editText.transformationMethod = StarTransformationMethod()
            } else if (TYPE_TEXT == inputType) {
                editText.inputType = InputType.TYPE_CLASS_TEXT
            } else if (TYPE_PHONE == inputType) {
                editText.inputType = InputType.TYPE_CLASS_PHONE
            } else if (NUMBER_PASSWORD == inputType) {
                editText.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER
                editText.transformationMethod = StarTransformationMethod()
            } else {
                editText.inputType = EditorInfo.TYPE_CLASS_PHONE
            }
            editText.id = i
            editText.setEms(1)
            editText.addTextChangedListener(this)
            addView(editText, i)

            mEditTextList!!.add(editText)
        }
    }

    private fun focus() {
        val count = childCount
        var editText: EditText
        for (i in 0 until count) {
            editText = getChildAt(i) as EditText
            if (editText.text.length < 1) {
                editText.requestFocus()
                return
            }
        }
    }

    private fun checkAndCommit() {
        val stringBuilder = StringBuilder()
        var full = true
        for (i in 0 until box) {
            val editText = getChildAt(i) as EditText
            val content = editText.text.toString()
            if (content.isEmpty()) {
                full = false
                break
            } else {
                stringBuilder.append(content)
            }
        }
        if (full) {
            if (listener != null) {
                listener!!.onComplete(stringBuilder.toString())
            }
        } else {
            if (listener != null) {
                listener!!.onChange(stringBuilder.toString())
            }
        }
    }

    /**
     * add by lixd
     */
    /**
     * add by lixd
     */
    @JvmOverloads
    fun clearAllInput(boxbg_red: Boolean = false) {
        if (boxbg_red) {
            if (null != mEditTextList && 0 < mEditTextList.size && null != mEditTextList[currentPosition]) {
                mEditTextList[currentPosition]!!.clearFocus()
            }
        }
        currentPosition = 0
        for (i in 0 until box) {
            val editText = getChildAt(i) as EditText
            if (null != editText) {
                editText.setText("")
                if (i == 0) {
                    editText.requestFocus()
                    editText.isEnabled = true
                    if (boxSelectBg != null) {
                        editText.background = boxSelectBg
                    } else {
                        editText.background = boxBgNormal
                    }
                } else {
                    editText.clearFocus()
                    editText.isEnabled = false
                    editText.background = boxBgNormal
                }
            }
        }
    }

    fun setOnCompleteListener(listener: Listener?) {
        this.listener = listener
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = childCount

        for (i in 0 until count) {
            val child = getChildAt(i)
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        if (count > 0) {
            val child = getChildAt(0)
            val cHeight = child.measuredHeight
            val cWidth = child.measuredWidth
            val maxH = cHeight + 2 * childVPadding
            val maxW = (cWidth + childHPadding) * box - childHPadding
            setMeasuredDimension(
                resolveSize(maxW, widthMeasureSpec),
                resolveSize(maxH, heightMeasureSpec)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            child.visibility = VISIBLE
            val cWidth = child.measuredWidth
            val cHeight = child.measuredHeight
            val cl = (i) * (cWidth + childHPadding)
            val cr = cl + cWidth
            val ct = childVPadding
            val cb = ct + cHeight
            child.layout(cl, ct, cr, cb)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        LogUtils.a(s, s.equals("＊"));
//        Spanned[] spanneds = mEditTextList.get(currentPosition).getText().getSpans(0, s.length(), Spanned.class);
//        if (inputType.equals(NUMBER_PASSWORD) || inputType.equals(TYPE_PASSWORD)) {
//            if (currentPosition > 0 && spanneds.length == 0) {
//                mEditTextList.get(currentPosition).setText(StringExKt.replaceSpan(s.toString(), "＊"));
//            }
//        }
        if (start == 0 && count >= 1 && currentPosition != mEditTextList!!.size - 1) {
            mEditTextList[currentPosition]!!.clearFocus()
            if (boxInputEdBg != null) {
                mEditTextList[currentPosition]!!.background = boxInputEdBg
            } else {
                mEditTextList[currentPosition]!!.background = boxBgNormal
            }
            currentPosition++
            mEditTextList[currentPosition]!!.isEnabled = true
            mEditTextList[currentPosition]!!.requestFocus()
            mEditTextList[currentPosition - 1]!!.isEnabled = false
            if (boxSelectBg != null) {
                mEditTextList[currentPosition]!!.background = boxSelectBg
            } else {
                mEditTextList[currentPosition]!!.background = boxBgNormal
            }
        }
    }

    override fun afterTextChanged(s: Editable) {
        if (s.length == 0) {
        } else {
//            focus();
            checkAndCommit()
        }
    }

    override fun onKey(view: View, keyCode: Int, event: KeyEvent): Boolean {
        val editText = view as EditText
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.text.length == 0) {
            val action = event.action
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                //add by lixd
                mEditTextList!![currentPosition]!!.background = boxBgNormal
                mEditTextList[currentPosition]!!.clearFocus()
                currentPosition--
                mEditTextList[currentPosition]!!.isEnabled = true
                if (boxSelectBg != null) {
                    mEditTextList[currentPosition]!!.background = boxSelectBg
                } else {
                    mEditTextList[currentPosition]!!.background = boxBgNormal
                }
                mEditTextList[currentPosition]!!.requestFocus()
                mEditTextList[currentPosition]!!.setText("")
                mEditTextList[currentPosition + 1]!!.isEnabled = false
            }
        }
        return false
    }

    interface Listener {
        fun onComplete(content: String?)
        fun onChange(content: String?)
    }

    companion object {
        private const val TYPE_NUMBER = "number"
        private const val TYPE_TEXT = "text"
        private const val TYPE_PASSWORD = "password"
        private const val NUMBER_PASSWORD = "numberPassword"
        private const val TYPE_PHONE = "phone"

        private const val TAG = "VerificationCodeInput"
    }
}