package com.phantomvk.democenter.dragDrop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.R
import kotlin.collections.ArrayList

class DragDropActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_drag_drop)

    val originList = ArrayList<DragDropModel>(100)
    repeat(100) { originList.add(DragDropModel()) }
    val replacedList = ArrayList(originList)

    val mainAdapter = DragDropAdapter(false, layoutInflater, originList, replacedList)
    val mainRecyclerView = findViewById<RecyclerView>(R.id.recycle_view_main)
    mainRecyclerView.adapter = mainAdapter
    mainRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    mainRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    mainAdapter.notifyItemRangeInserted(0, originList.size)

    val manager = SyncLinearLayoutManager(this, RecyclerView.HORIZONTAL, false, mainRecyclerView)
    val minorAdapter = DragDropAdapter(true, layoutInflater, originList, replacedList)
    val minorRecyclerView = findViewById<RecyclerView>(R.id.recycle_view_minor)
    minorRecyclerView.adapter = minorAdapter
    minorRecyclerView.layoutManager = manager
    minorRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
    ItemTouchHelper(ItemTouchHelperCallback(minorAdapter)).attachToRecyclerView(minorRecyclerView)
    minorAdapter.notifyItemRangeInserted(0, originList.size)
  }
}