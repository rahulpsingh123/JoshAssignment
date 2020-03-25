package com.l.joshassignment.helper

import android.os.SystemClock
import android.view.View

/**
 * View extensions
 */

fun View.click(debounceTime: Long = 300L, action: (view: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

