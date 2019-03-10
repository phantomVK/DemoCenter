package com.phantomvk.democenter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.phantomvk.democenter.activity.BubbleShapeActivity
import com.phantomvk.democenter.util.startActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To BubbleShapeActivity.
        btnBubbleShape.startActivity<BubbleShapeActivity>()
    }
}
