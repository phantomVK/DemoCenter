package com.phantomvk.democenter.dragdrop

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SyncLinearLayoutManager(
  context: Context,
  orientation: Int,
  reverseLayout: Boolean,
  private val mainRecyclerView: RecyclerView
) : LinearLayoutManager(context, orientation, reverseLayout) {

  override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
    mainRecyclerView.scrollBy(dx, 0)
    return super.scrollHorizontallyBy(dx, recycler, state)
  }

  override fun scrollToPosition(position: Int) {
    mainRecyclerView.scrollToPosition(position)
    super.scrollToPosition(position)
  }

  override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
    mainRecyclerView.smoothScrollToPosition(position)
    super.smoothScrollToPosition(recyclerView, state, position)
  }
}
