package com.phantomvk.democenter.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape
import android.support.annotation.ColorInt

class BubbleShape constructor(var arrowDirection: DIRECTION = BubbleShape.DIRECTION.START,
                              @ColorInt var solidColor: Int,
                              @ColorInt var strokeColor: Int,
                              var strokeWidth: Int,
                              var cornerRadius: Int,
                              var arrowWidth: Int,
                              var arrowHeight: Int,
                              var arrowMarginTop: Int) : Shape() {
    /**
     * The path for upper area.
     */
    private var mUpperPath = Path()

    /**
     * The path for lower area.
     */
    private var mLowerPath = Path()

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

        // Upper area.
        canvas.drawPath(mUpperPath, paint)

        // Middle area.
        val rectF = RectF(arrowWidth.toFloat(), mUpperHeightFA, width, height - cornerRadius)
        canvas.drawRect(rectF, paint)

        // Lower area.
        canvas.drawPath(mLowerPath, paint)

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
        val leftTop = RectF(arrowWidth + strokeOffset, strokeOffset, arrowWidth + cornerRadius - strokeOffset, cornerRadius - strokeOffset)
        canvas.drawArc(leftTop, 180F, 90F, false, paint)
        canvas.drawLine(arrowWidth + cornerRadius - radiusOffset, strokeOffset, width - cornerRadius + radiusOffset, strokeOffset, paint)

        // Right top corner and the right line.
        val rightTop = RectF(width - cornerRadius + strokeOffset, strokeOffset, width - strokeOffset, cornerRadius - strokeOffset)
        canvas.drawArc(rightTop, 270F, 90F, false, paint)
        canvas.drawLine(width - strokeOffset, cornerRadius - radiusOffset, width - strokeOffset, height - cornerRadius + radiusOffset, paint)

        // Right bottom corner and the bottom line.
        val rightBottom = RectF(width - cornerRadius + strokeOffset, height - cornerRadius + strokeOffset, width - strokeOffset, height - strokeOffset)
        canvas.drawArc(rightBottom, 0F, 90F, false, paint)
        canvas.drawLine(width - cornerRadius + radiusOffset, height - strokeOffset, arrowWidth + cornerRadius - radiusOffset, height - strokeOffset, paint)

        // Left bottom corner and the left lower line.
        val leftBottom = RectF(arrowWidth + strokeOffset, height - cornerRadius + strokeOffset, arrowWidth + cornerRadius - strokeOffset, height - strokeOffset)
        canvas.drawArc(leftBottom, 90F, 90F, false, paint)
        canvas.drawLine(arrowWidth + strokeOffset, height - cornerRadius + radiusOffset, arrowWidth + strokeOffset, upperHeightFA, paint)

        // Arrow shape and the left upper line.
        canvas.drawLine(arrowWidth + strokeOffset, upperHeightFA, strokeOffset, upperHeightHA, paint)
        canvas.drawLine(strokeOffset, upperHeightHA, arrowWidth + strokeOffset, upperHeightNA, paint)
        canvas.drawLine(arrowWidth + strokeOffset, mUpperHeightNA, arrowWidth + strokeOffset, cornerRadius - radiusOffset, paint)
    }

    override fun onResize(width: Float, height: Float) {
        resizeTopPath(width)
        resizeBottomPath(width, height)
    }

    private fun resizeTopPath(width: Float) {
        val cornerRadius = cornerRadius.toFloat()
        val arrowWidth = arrowWidth.toFloat()
        val upperHeightNA = mUpperHeightNA
        val upperHeightHA = mUpperHeightHA
        val upperHeightFA = mUpperHeightFA

        mUpperPath.reset()

        // Arrow
        mUpperPath.moveTo(arrowWidth, upperHeightFA)
        mUpperPath.lineTo(0F, upperHeightHA)
        mUpperPath.lineTo(arrowWidth, upperHeightNA)

        // Left upper line.
        mUpperPath.lineTo(arrowWidth, cornerRadius)

        // Left top corner.
        val leftTop = RectF(arrowWidth, 0F, arrowWidth + cornerRadius, cornerRadius)
        mUpperPath.arcTo(leftTop, 180F, 90F)

        // Top line.
        mUpperPath.lineTo(width - cornerRadius, 0F)

        // Right top corner.
        val rightTop = RectF(width - cornerRadius, 0F, width, cornerRadius)
        mUpperPath.arcTo(rightTop, 270F, 90F)

        // Right line.
        mUpperPath.lineTo(width, upperHeightFA)
    }

    private fun resizeBottomPath(width: Float, height: Float) {
        val cornerRadius = cornerRadius.toFloat()
        val arrowWidth = arrowWidth.toFloat()

        mLowerPath.reset()

        // Right bottom corner.
        mLowerPath.moveTo(width, height - cornerRadius)
        val rightBottom = RectF(width - cornerRadius, height - cornerRadius, width, height)
        mLowerPath.arcTo(rightBottom, 0F, 90F)

        // Bottom line.
        mLowerPath.lineTo((arrowWidth + cornerRadius), height)

        // Left bottom corner.
        val leftBottom = RectF(arrowWidth, height - cornerRadius, (arrowWidth + cornerRadius), height)
        mLowerPath.arcTo(leftBottom, 90F, 90F)

        // Left lower line.
        mLowerPath.lineTo(arrowWidth, height - cornerRadius)
    }

    /**
     * Deep clone as a new object.
     */
    override fun clone(): BubbleShape {
        val shape = super.clone() as BubbleShape
        shape.mUpperPath = Path()
        shape.mLowerPath = Path()
        return shape
    }

    /**
     * Arrow direction.
     */
    enum class DIRECTION { START, END }
}
