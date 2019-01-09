package com.phantomvk.democenter.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button

inline fun <reified T : Activity> Button.startActivity() {
    setOnClickListener { context.startActivity(Intent(context, T::class.java)) }
}

fun Context.dip(value: Float) = (value * resources.displayMetrics.density).toInt()
