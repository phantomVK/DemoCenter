package com.phantomvk.democenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.phantomvk.democenter.activity.BubbleShapeActivity
import com.phantomvk.democenter.databinding.ActivityMainBinding
import com.phantomvk.democenter.util.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.btnBubbleShape.startActivity<BubbleShapeActivity>()
    }
}
