package com.phantomvk.democenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.phantomvk.democenter.aidl.AidlActivity
import com.phantomvk.democenter.bubbleShape.BubbleShapeActivity
import com.phantomvk.democenter.databinding.ActivityMainBinding
import com.phantomvk.democenter.dragDrop.DragDropActivity

inline fun <reified T : Activity> Button.startActivity() {
    setOnClickListener { context.startActivity(Intent(context, T::class.java)) }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.btnBubbleShape.startActivity<BubbleShapeActivity>()
        binding.btnAIDL.startActivity<AidlActivity>()
        binding.btnDragDrop.startActivity<DragDropActivity>()
    }
}
