package com.stars.any_view.view

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText

/**
 * Author: Stars
 * Created Date: 2024/8/1
 * Description: 输入框的粘贴监听、键盘传入监听
 */
class ChatInputView: AppCompatEditText {

    private var onPasteCallback: OnPasteCallback? = null
    private var customInputConnection: CustomInputConnection? = null

    init {
        customInputConnection = CustomInputConnection(null, true)
    }

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onTextContextMenuItem(id: Int): Boolean {
        return when (id) {
            android.R.id.paste -> onPasteCallback?.onPaste() == true
            else -> {
                super.onTextContextMenuItem(id)
            }
        }
    }

    // 增加监控，解决某些键盘上的剪切板复制过来的内容
    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        customInputConnection?.setTarget(super.onCreateInputConnection(outAttrs))
        return customInputConnection
    }

    fun setPasteListener(callback: OnPasteCallback) {
        this.onPasteCallback = callback
        customInputConnection?.setPasteListener(this.onPasteCallback)
    }

    interface OnPasteCallback {
        fun onPaste(content: String? = null): Boolean
    }

    class CustomInputConnection(target: InputConnection?, mutable: Boolean) : InputConnectionWrapper(target, mutable) {
        private var callback: OnPasteCallback? = null
        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            return if (!text.isNullOrEmpty()) {
                val result = Regex("\\[wowo_emoji_\\d+]").findAll(text)
                if (result.count() > 0) {
                    callback?.onPaste(text.toString()) == true
                }else {
                    super.commitText(text, newCursorPosition)
                }
            }else {
                super.commitText(text, newCursorPosition)
            }
        }

        fun setPasteListener(callback: OnPasteCallback?) {
            this.callback = callback
        }
    }
}