package com.phantomvk.democenter.drawer

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.ColorInt

class AuxiliaryLineDrawer(
  var orientation: Int,
  @ColorInt color: Int,
  style: Paint.Style,
  strokeWidth: Float,
  effect: DashPathEffect,
) {
  private val paint = Paint()
  private val path = Path()

  var isEnabled = true

  init {
    paint.isAntiAlias = true
    paint.color = color
    paint.style = style
    paint.strokeWidth = strokeWidth
    paint.pathEffect = effect
  }

  fun onDraw(canvas: Canvas, width: Float, height: Float) {
    if (!isEnabled) return

    if ((orientation and HORIZONTAL) == HORIZONTAL) {
      val center = height / 2
      path.reset()
      path.moveTo(0F, center)
      path.lineTo(width, center)
      canvas.drawPath(path, paint)
    }

    if ((orientation and VERTICAL) == VERTICAL) {
      val center = width / 2
      path.reset()
      path.moveTo(center, 0F)
      path.lineTo(center, height)
      canvas.drawPath(path, paint)
    }
  }

  companion object {
    const val HORIZONTAL = 1
    const val VERTICAL = 2
  }
}