package com.phantomvk.democenter.utility

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object Hardware {
  @JvmStatic
  fun vibrate(context: Context, milliseconds: Long) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
      (vibratorManager as VibratorManager).defaultVibrator
    } else {
      context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
    }

    when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
        val vibe = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        vibrator.vibrate(vibe)
      }

      Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
        val vibe = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibe)
      }

      else -> {
        vibrator.vibrate(milliseconds)
      }
    }
  }
}