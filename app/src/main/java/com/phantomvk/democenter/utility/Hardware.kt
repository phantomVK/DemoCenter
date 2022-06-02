package com.phantomvk.democenter.utility

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object Hardware {
  @JvmStatic
  fun vibrate(context: Context, milliseconds: Long) {
    val service = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
        val vibe = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        service.vibrate(vibe)
      }

      Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
        val vibe = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
        service.vibrate(vibe)
      }

      else -> {
        service.vibrate(milliseconds)
      }
    }
  }
}