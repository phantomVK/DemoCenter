package com.phantomvk.democenter.dragdrop

import android.graphics.Color
import kotlin.random.Random

class DragDropModel(
  val colorInt: Int = Random.nextInt(Color.BLACK, Color.WHITE),
  val duration: Double = Random.nextDouble(1.0, 100.0)
)