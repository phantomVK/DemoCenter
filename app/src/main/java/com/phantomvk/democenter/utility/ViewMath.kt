package com.phantomvk.democenter.utility

import android.graphics.PointF
import kotlin.math.atan2

/**
 * Calculate the rotation angle of view by two points.
 *
 * @return angle in float
 */
fun getRotationAngle(
  rightTop: PointF,
  leftTop: PointF
): Float {
  return getRotationAngle(
    rightTop.y, leftTop.y,
    rightTop.x, leftTop.x
  )
}

/**
 * Calculate the rotation angle of view by two points.
 *
 * @return angle in float
 */
fun getRotationAngle(
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

/**
 * Get the center coordinates of view by two points.
 *
 * @return PointF
 */
fun getCenterCoordinates(
  leftTop: PointF,
  rightBottom: PointF
): PointF {
  val x = leftTop.x + rightBottom.x
  val y = leftTop.y + rightBottom.y
  return PointF(x / 2, y / 2)
}

/**
 * Get the center coordinates of view by two points.
 *
 * @param saveTo result will save to this given PointF
 */
fun getCenterCoordinates(
  leftTop: PointF,
  rightBottom: PointF,
  saveTo: PointF
) {
  val x = leftTop.x + rightBottom.x
  val y = leftTop.y + rightBottom.y
  saveTo.set(x / 2, y / 2)
}