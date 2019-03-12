package com.phantomvk.democenter.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.shapes.Shape
import android.support.annotation.ColorInt

/**
 * Draw the shape of the message contents like bubbles.
 */
class BubbleShape constructor(var arrowDirection: DIRECTION = BubbleShape.DIRECTION.START,
                              @ColorInt var solidColor: Int,
                              @ColorInt var strokeColor: Int,
                              var strokeWidth: Float,
                              var cornerRadius: Float,
                              var arrowWidth: Float,
                              var arrowHeight: Float,
                              var arrowMarginTop: Float) : Shape() {
    /**
     * The path to fill canvas.
     */
    private var mPathFill = Path()

    /**
     * The path to draw stroke.
     */
    private var mPathStroke = Path()

    /**
     * RectF for reusing.
     */
    private var mRectF = RectF()

    /**
     * Stroke offset.
     */
    private var mStrokeOffset = strokeWidth / 2

    /**
     * The height of upper area with no arrow.
     */
    private val mUpperHeightNA = cornerRadius + arrowMarginTop + mStrokeOffset

    /**
     * The height of upper area with half a arrow.
     */
    private val mUpperHeightHA = mUpperHeightNA + (arrowHeight / 2)

    /**
     * The height of upper area with a full arrow.
     */
    private val mUpperHeightFA = mUpperHeightNA + arrowHeight

    override fun draw(canvas: Canvas, paint: Paint) {
        // Set arrow direction.
        if (arrowDirection == DIRECTION.END) {
            canvas.scale(-1F, 1F, width / 2, height / 2)
        }

        canvas.save()
        fillCanvas(canvas, paint)
        drawStroke(canvas, paint)
        canvas.restore()
    }

    private fun fillCanvas(canvas: Canvas, paint: Paint) {
        paint.color = solidColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.isDither = true

        canvas.drawPath(mPathFill, paint)
    }

    private fun drawStroke(canvas: Canvas, paint: Paint) {
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = strokeWidth

        canvas.drawPath(mPathStroke, paint)
    }

    /**
     * Resize when width or height is changed.
     */
    override fun onResize(width: Float, height: Float) {
        resizeStrokePath(width, height)
        resizeFillPath(width, height)
    }

    private fun resizeFillPath(width: Float, height: Float) {
        val cornerRadius = cornerRadius
        val arrowWidth = arrowWidth

        mPathFill.reset()

        // Draw arrow.
        mPathFill.moveTo(arrowWidth, mUpperHeightFA)
        mPathFill.lineTo(0F, mUpperHeightHA)
        mPathFill.lineTo(arrowWidth, mUpperHeightNA)
        mPathFill.lineTo(arrowWidth, cornerRadius)

        // Upper left corner and the upper line.
        mRectF.set(arrowWidth, 0F, arrowWidth + cornerRadius, cornerRadius)
        mPathFill.arcTo(mRectF, 180F, 90F)
        mPathFill.lineTo(width - cornerRadius, 0F)

        // Upper right corner and the right line.
        mRectF.set(width - cornerRadius, 0F, width, cornerRadius)
        mPathFill.arcTo(mRectF, 270F, 90F)
        mPathFill.lineTo(width, height - cornerRadius)

        // Bottom right corner and the bottom line.
        mRectF.set(width - cornerRadius, height - cornerRadius, width, height)
        mPathFill.arcTo(mRectF, 0F, 90F)
        mPathFill.lineTo((arrowWidth + cornerRadius), height)

        // Bottom left corner.
        mRectF.set(arrowWidth, height - cornerRadius, arrowWidth + cornerRadius, height)
        mPathFill.arcTo(mRectF, 90F, 90F)

        mPathFill.close()
    }

    private fun resizeStrokePath(width: Float, height: Float) {
        val strokeOffset = mStrokeOffset
        val cornerRadius = cornerRadius
        val arrowWidth = arrowWidth

        mPathStroke.reset()

        // Arrow and the upper left line.
        mPathStroke.moveTo(arrowWidth + strokeOffset, mUpperHeightFA)
        mPathStroke.lineTo(strokeOffset, mUpperHeightHA)
        mPathStroke.lineTo(arrowWidth + strokeOffset, mUpperHeightNA)
        mPathStroke.lineTo(arrowWidth + strokeOffset, cornerRadius)

        // Upper left corner and the upper line.
        mRectF.set(arrowWidth + strokeOffset, strokeOffset, arrowWidth + cornerRadius - strokeOffset, cornerRadius - strokeOffset)
        mPathStroke.arcTo(mRectF, 180F, 90F)
        mPathStroke.lineTo(width - cornerRadius, strokeOffset)

        // Upper right corner and the right line.
        mRectF.set(width - cornerRadius + strokeOffset, strokeOffset, width - strokeOffset, cornerRadius - strokeOffset)
        mPathStroke.arcTo(mRectF, 270F, 90F)
        mPathStroke.lineTo(width - strokeOffset, height - cornerRadius)

        // Bottom right corner and the bottom line.
        mRectF.set(width - cornerRadius + strokeOffset, height - cornerRadius + strokeOffset, width - strokeOffset, height - strokeOffset)
        mPathStroke.arcTo(mRectF, 0F, 90F)
        mPathStroke.lineTo((arrowWidth + cornerRadius), height - strokeOffset)

        // Bottom left corner.
        mRectF.set(arrowWidth + strokeOffset, height - cornerRadius + strokeOffset, arrowWidth + cornerRadius - strokeOffset, height - strokeOffset)
        mPathStroke.arcTo(mRectF, 90F, 90F)

        mPathStroke.close()
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
