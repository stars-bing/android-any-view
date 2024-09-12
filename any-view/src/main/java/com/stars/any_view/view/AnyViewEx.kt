package com.stars.any_view.view

import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.BindingAdapter

/**
 * Author: Stars
 * Created Date: 2024/9/12
 * Description: 拓展View的方法/BindingAdapter注解
 */


// View 的点击事件处理
@BindingAdapter("bindClick")
fun View.bindClick(action: (() -> Unit)?) {
    if (action != null) {
        setOnClickListener { action() }
    }
}

// 绑定View加载完成后通知
@BindingAdapter("bindLoadingComplete")
fun View.bindLoadingComplete(action: ((View) -> Unit)?) {
    if (action == null) {
        return
    }
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            action.invoke(this@bindLoadingComplete)
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

// 是否启用
@BindingAdapter("isEnable")
fun View.isEnable(isEnable: Boolean) {
    isEnabled = isEnable
}

// 是否显示
@BindingAdapter("isInVisible")
fun View.isInVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

// 是否选择
@BindingAdapter("bindIsSelected")
fun View.bindIsSelected(bindIsSelected: Boolean?) {
    if (bindIsSelected == null) return
    isSelected = bindIsSelected
}

// 是否显示
@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

// 长按点击
@BindingAdapter("bindLongClick")
fun View.bindLongClick(action: (() -> Unit)?) {
    if (action != null) {
        setOnLongClickListener {
            action()
            true
        }
    }
}

// 设置背景资源
@BindingAdapter("bindBackgroundRes")
fun View.bindBackgroundRes(drawableId: Int?) {
    if (drawableId != null && drawableId > 0) {
        setBackgroundResource(drawableId)
    }
}

// 设置背景颜色
@BindingAdapter("bindBackground")
fun View.bindBackground(color: Int?) {
    if (color != null) {
        setBackgroundColor(color)
    }
}

// 圆形进度设置
@BindingAdapter("bindRingProgress")
fun RingProgressBar.bindRingProgress(progress: Int) {
    setProgress(progress)
}

