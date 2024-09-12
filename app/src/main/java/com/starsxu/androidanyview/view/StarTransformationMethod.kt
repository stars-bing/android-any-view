package com.starsxu.androidanyview.view

import android.text.method.PasswordTransformationMethod
import android.view.View

/**
 * Author: Stars
 * Created Date: 2024/9/12
 * Description:
 */
class StarTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return PasswordCharSequence(source)
    }

    private class PasswordCharSequence // Store char sequence
        (private val mSource: CharSequence) : CharSequence {

        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return '＊'
        }

        override fun subSequence(start: Int, end: Int): CharSequence {
            return mSource.subSequence(start, end) // Return a sub sequence of stars
        }
    }
}
