package com.phantomvk.democenter.dragDrop

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.utility.Hardware

class ItemTouchHelperCallback(
  private val minorAdapter: DragDropAdapter
) : ItemTouchHelper.Callback() {

  override fun getMovementFlags(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder
  ): Int {
    return makeMovementFlags(
      ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
      0
    )
  }

  override fun onMove(
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ): Boolean {
    minorAdapter.onMove(viewHolder, target)
    return true
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
  }

  override fun isItemViewSwipeEnabled(): Boolean {
    return false
  }

  override fun isLongPressDragEnabled(): Boolean {
    return true
  }

  override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    super.onSelectedChanged(viewHolder, actionState)
    if (viewHolder is DragDropAdapter.MainViewHolder) {
      Hardware.vibrate(viewHolder.itemView.context, 20)
      viewHolder.setDurationTextViewAlpha(actionState != ItemTouchHelper.ACTION_STATE_IDLE)
      viewHolder.scaleCoverView(true)
    }
  }

  override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
    super.clearView(recyclerView, viewHolder)
    if (viewHolder is DragDropAdapter.MainViewHolder) {
      viewHolder.setDurationText()
      viewHolder.setDurationTextViewAlpha(recyclerView.isComputingLayout)
      viewHolder.scaleCoverView(false)
    }
  }
}