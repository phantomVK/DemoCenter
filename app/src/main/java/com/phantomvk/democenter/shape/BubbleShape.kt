package com.phantomvk.democenter.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape
import android.support.annotation.ColorInt

/**
 * Draw the shape of the message contents like bubbles. This class used for main thread only,
 * because properties are shared in different cloned objects to reduce memory usage.
 */
class BubbleShape constructor(var arrowDirection: DIRECTION = BubbleShape.DIRECTION.START,
                              @ColorInt var solidColor: Int,
                              @ColorInt var strokeColor: Int,
                              var strokeWidth: Int,
                              var cornerRadius: Int,
                              var arrowWidth: Int,
                              var arrowHeight: Int,
                              var arrowMarginTop: Int) : Shape() {
    /**
     * The path for to fill.
     */
    private var mPathFill = Path()

    /**
     * RectF for reusing.
     */
    private var mRectF = RectF()

    /**
     * Stroke offset.
     */
    private var mStrokeOffset = (strokeWidth ushr 1).toFloat()

    /**
     * Corner radius offset.
     */
    private var mRadiusOffset = (cornerRadius ushr 1).toFloat()

    /**
     * The height of upper area with no arrow.
     */
    private val mUpperHeightNA = cornerRadius + arrowMarginTop + mStrokeOffset

    /**
     * The height of upper area with half arrow.
     */
    private val mUpperHeightHA = mUpperHeightNA + (arrowHeight ushr 1).toFloat()

    /**
     * The height of upper area with full arrow.
     */
    private val mUpperHeightFA = mUpperHeightNA + arrowHeight

    /**
     * Latest resized width.
     */
    private var mResizedWidth = -1F

    /**
     * Latest resized height.
     */
    private var mResizedHeight = -1F

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = solidColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true

        canvas.save()

        // Set arrow direction.
        if (arrowDirection == DIRECTION.END) {
            canvas.scale(-1F, 1F, width / 2, height / 2)
        }

        // Fill path.
        canvas.drawPath(mPathFill, paint)

        // Draw stroke.
        drawStroke(canvas, paint)

        canvas.restore()
    }

    private fun drawStroke(canvas: Canvas, paint: Paint) {
        val strokeOffset = mStrokeOffset
        val radiusOffset = mRadiusOffset
        val cornerRadius = cornerRadius
        val arrowWidth = arrowWidth
        val upperHeightNA = mUpperHeightNA
        val upperHeightHA = mUpperHeightHA
        val upperHeightFA = mUpperHeightFA

        // Set paint.
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = strokeWidth.toFloat()

        // Left top corner and the top line.
        mRectF.set(arrowWidth + strokeOffset, strokeOffset, arrowWidth + cornerRadius - strokeOffset, cornerRadius - strokeOffset)
        canvas.drawArc(mRectF, 180F, 90F, false, paint)
        canvas.drawLine(arrowWidth + cornerRadius - radiusOffset, strokeOffset, width - cornerRadius + radiusOffset, strokeOffset, paint)

        // Right top corner and the right line.
        mRectF.set(width - cornerRadius + strokeOffset, strokeOffset, width - strokeOffset, cornerRadius - strokeOffset)
        canvas.drawArc(mRectF, 270F, 90F, false, paint)
        canvas.drawLine(width - strokeOffset, cornerRadius - radiusOffset, width - strokeOffset, height - cornerRadius + radiusOffset, paint)

        // Right bottom corner and the bottom line.
        mRectF.set(width - cornerRadius + strokeOffset, height - cornerRadius + strokeOffset, width - strokeOffset, height - strokeOffset)
        canvas.drawArc(mRectF, 0F, 90F, false, paint)
        canvas.drawLine(width - cornerRadius + radiusOffset, height - strokeOffset, arrowWidth + cornerRadius - radiusOffset, height - strokeOffset, paint)

        // Left bottom corner and the left lower line.
        mRectF.set(arrowWidth + strokeOffset, height - cornerRadius + strokeOffset, arrowWidth + cornerRadius - strokeOffset, height - strokeOffset)
        canvas.drawArc(mRectF, 90F, 90F, false, paint)
        canvas.drawLine(arrowWidth + strokeOffset, height - cornerRadius + radiusOffset, arrowWidth + strokeOffset, upperHeightFA, paint)

        // Arrow shape and the left upper line.
        canvas.drawLine(arrowWidth + strokeOffset, upperHeightFA, strokeOffset, upperHeightHA, paint)
        canvas.drawLine(strokeOffset, upperHeightHA, arrowWidth + strokeOffset, upperHeightNA, paint)
        canvas.drawLine(arrowWidth + strokeOffset, mUpperHeightNA, arrowWidth + strokeOffset, cornerRadius - radiusOffset, paint)
    }

    /**
     * Resize when width or height is changed.
     */
    override fun onResize(width: Float, height: Float) {
        if (mResizedHeight != height || mResizedWidth != width) {
            resizePath(width, height)

            mResizedWidth = width
            mResizedHeight = height
        }
    }

    private fun resizePath(width: Float, height: Float) {
        val cornerRadius = cornerRadius.toFloat()
        val arrowWidth = arrowWidth.toFloat()

        mPathFill.reset()

        // Draw arrow.
        mPathFill.moveTo(arrowWidth, mUpperHeightFA)
        mPathFill.lineTo(0F, mUpperHeightHA)
        mPathFill.lineTo(arrowWidth, mUpperHeightNA)

        // Upper left line.
        mPathFill.lineTo(arrowWidth, cornerRadius)

        // Upper left corner.
        mRectF.set(arrowWidth, 0F, arrowWidth + cornerRadius, cornerRadius)
        mPathFill.arcTo(mRectF, 180F, 90F)

        // Upper line.
        mPathFill.lineTo(width - cornerRadius, 0F)

        // Upper right corner.
        mRectF.set(width - cornerRadius, 0F, width, cornerRadius)
        mPathFill.arcTo(mRectF, 270F, 90F)

        // Right line.
        mPathFill.lineTo(width, height - cornerRadius)

        // Bottom right corner.
        mRectF.set(width - cornerRadius, height - cornerRadius, width, height)
        mPathFill.arcTo(mRectF, 0F, 90F)

        // Bottom line.
        mPathFill.lineTo((arrowWidth + cornerRadius), height)

        // Bottom left corner.
        mRectF.set(arrowWidth, height - cornerRadius, (arrowWidth + cornerRadius), height)
        mPathFill.arcTo(mRectF, 90F, 90F)

        // Lower left line.
        mPathFill.close()
    }

    /**
     * Shallow clone as a new object.
     */
    override fun clone(): BubbleShape = super.clone() as BubbleShape

    /**
     * Arrow direction.
     */
    enum class DIRECTION { START, END }
}
