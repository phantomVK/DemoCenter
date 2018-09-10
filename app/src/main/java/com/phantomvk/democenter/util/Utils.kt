package com.phantomvk.democenter.util

import android.content.Context

fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()
