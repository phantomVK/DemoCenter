package com.phantomvk.democenter.bubbleshape

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phantomvk.democenter.bubbleshape.databinding.ActivityBubbleShapeBinding

fun Context.dip(value: Float) = value * resources.displayMetrics.density

class BubbleShapeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBubbleShapeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Arrow direction: to left.
        val bubbleShapeStart = BubbleShape(
                solidColor = 0x8800FF00.toInt(),
                strokeColor = 0xFFCFCFCF.toInt(),
                strokeWidth = dip(1F),
                arrowWidth = dip(6F),
                arrowMarginTop = dip(1F),
                arrowHeight = dip(12F),
                cornerRadius = dip(10F))
        binding.bubbleViewStart.background = BubbleShapeDrawable(bubbleShapeStart, dip(6F).toInt(), true)

        // Arrow direction: to right.
        val bg = BubbleShapeDrawable.bubbleStateListDrawable(this@BubbleShapeActivity, bubbleShapeStart.clone(), true)
        binding.bubbleViewClickable.background = bg
        binding.bubbleViewClickable.setOnClickListener {}

        // Big.
        val bubbleBig = BubbleShape(arrowDirection = BubbleShape.DIRECTION.START,
                solidColor = 0x88FF0000.toInt(),
                strokeColor = 0xAACFCFCF.toInt(),
                strokeWidth = dip(20F),
                arrowWidth = dip(30F),
                arrowMarginTop = dip(10F),
                arrowHeight = dip(50F),
                cornerRadius = dip(80F))
        binding.bubbleViewAlpha.background = ShapeDrawable(bubbleBig)
    }
}
