package com.phantomvk.democenter.utility

import android.graphics.PointF
import kotlin.math.atan2

class ViewMath {
  /**
   * Calculate the rotated degree of view by two points.
   *
   * @return degree in float
   */
  fun rotatedDegree(
    rightTop: PointF,
    leftTop: PointF
  ): Float {
    return rotatedDegree(
      rightTop.y, leftTop.y,
      rightTop.x, leftTop.x
    )
  }

  /**
   * Calculate the rotated degree of view by two points.
   *
   * @return degree in float
   */
  fun rotatedDegree(
    rightTopX: Float,
    rightTopY: Float,
    leftTopX: Float,
    leftTopY: Float,
  ): Float {
    val radians = atan2(
      rightTopY - leftTopY,
      rightTopX - leftTopX
    ).toDouble()

    return Math.toDegrees(radians).toFloat()
  }
}