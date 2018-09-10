package com.phantomvk.democenter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.phantomvk.democenter.shape.BubbleShape
import com.phantomvk.democenter.shape.BubbleShapeDrawable
import com.phantomvk.democenter.util.dip
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bubbleShapeStart = BubbleShape(arrowDirection = BubbleShape.DIRECTION.START,
                solidColor = 0x8800FF00.toInt(),
                strokeColor = 0xFFCFCFCF.toInt(),
                strokeWidth = dip(1F),
                arrowWidth = dip(6F),
                arrowMarginTop = dip(1F),
                arrowHeight = dip(12F),
                cornerRadius = dip(10F))

        // Arrow direction: to left.
        bubbleViewStart.background = BubbleShapeDrawable(bubbleShapeStart, dip(6F), true)

        val bubbleShapeEnd = bubbleShapeStart.clone().apply { arrowDirection = BubbleShape.DIRECTION.END }

        val darkColor = Color.argb(Color.alpha(bubbleShapeStart.solidColor),
                (Color.red(bubbleShapeStart.solidColor) * 0.9).toInt(),
                (Color.green(bubbleShapeStart.solidColor) * 0.9).toInt(),
                (Color.blue(bubbleShapeStart.solidColor) * 0.9).toInt())

        bubbleViewClickable.apply {
            background = bubbleStateListDrawable(this@MainActivity, bubbleShapeEnd, darkColor)
            setOnClickListener {}
        }

        BubbleShape(arrowDirection = BubbleShape.DIRECTION.START,
                solidColor = 0x88FF0000.toInt(),
                strokeColor = 0xAACFCFCF.toInt(),
                strokeWidth = dip(10F),
                arrowWidth = dip(20F),
                arrowMarginTop = dip(10F),
                arrowHeight = dip(40F),
                cornerRadius = dip(40F)).apply { bubbleViewAlpha.background = ShapeDrawable(this) }
    }
}

fun bubbleStateListDrawable(context: Context, shape: BubbleShape, darkColor: Int) = StateListDrawable().apply {
    val drawable = BubbleShapeDrawable(shape, context.dip(6F), true)
    val darkShape = shape.clone().apply { solidColor = darkColor }
    val darkDrawable = BubbleShapeDrawable(darkShape, context.dip(6F), true)
    addState(intArrayOf(android.R.attr.state_pressed), darkDrawable)
    addState(intArrayOf(android.R.attr.state_selected), darkDrawable)
    addState(intArrayOf(), drawable)
}
