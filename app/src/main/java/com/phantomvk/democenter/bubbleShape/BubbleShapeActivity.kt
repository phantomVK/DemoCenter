package com.phantomvk.democenter.bubbleShape

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phantomvk.democenter.R
import kotlinx.android.synthetic.main.activity_bubble_shape.*

fun Context.dip(value: Float) = value * resources.displayMetrics.density

class BubbleShapeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble_shape)

        // Arrow direction: to left.
        val bubbleShapeStart = BubbleShape(
                solidColor = 0x8800FF00.toInt(),
                strokeColor = 0xFFCFCFCF.toInt(),
                strokeWidth = dip(1F),
                arrowWidth = dip(6F),
                arrowMarginTop = dip(1F),
                arrowHeight = dip(12F),
                cornerRadius = dip(10F))
        bubbleViewStart.background = BubbleShapeDrawable(bubbleShapeStart, dip(6F).toInt(), true)

        // Arrow direction: to right.
        val bg = BubbleShapeDrawable.bubbleStateListDrawable(this@BubbleShapeActivity, bubbleShapeStart.clone(), true)
        bubbleViewClickable.background = bg
        bubbleViewClickable.setOnClickListener {}

        // Big.
        val bubbleBig = BubbleShape(arrowDirection = BubbleShape.DIRECTION.START,
                solidColor = 0x88FF0000.toInt(),
                strokeColor = 0xAACFCFCF.toInt(),
                strokeWidth = dip(20F),
                arrowWidth = dip(30F),
                arrowMarginTop = dip(10F),
                arrowHeight = dip(50F),
                cornerRadius = dip(80F))
        bubbleViewAlpha.background = ShapeDrawable(bubbleBig)
    }
}
