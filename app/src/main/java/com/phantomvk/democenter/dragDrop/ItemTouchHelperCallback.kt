package com.phantomvk.democenter.dragDrop

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.utility.Hardware
import kotlin.math.abs

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
      viewHolder.updateAnimation(actionState != ItemTouchHelper.ACTION_STATE_IDLE)
    }
  }

  override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
    super.clearView(recyclerView, viewHolder)
    if (viewHolder is DragDropAdapter.MainViewHolder) {
      viewHolder.setDurationText()
      viewHolder.updateAnimation(recyclerView.isComputingLayout)
    }
  }

  override fun chooseDropTarget(
    selected: RecyclerView.ViewHolder,
    dropTargets: MutableList<RecyclerView.ViewHolder>,
    curX: Int,
    curY: Int
  ): RecyclerView.ViewHolder? {
    val right = curX + selected.itemView.width
    val bottom = curY + selected.itemView.height
    var winner: RecyclerView.ViewHolder? = null
    var winnerScore = -1
    val dx = curX - selected.itemView.left
    val dy = curY - selected.itemView.top
    val targetsSize = dropTargets.size
    for (i in 0 until targetsSize) {
      val target = dropTargets[i]
      if (dx > 0) {
        val diff = target.itemView.right - right - (selected.itemView.width / 2)
        if (diff < 0 && target.itemView.right > selected.itemView.right) {
          val score = abs(diff)
          if (score > winnerScore) {
            winnerScore = score
            winner = target
          }
        }
      }
      if (dx < 0) {
        val diff = target.itemView.left - curX + (selected.itemView.width / 2)
        if (diff > 0 && target.itemView.left < selected.itemView.left) {
          val score = abs(diff)
          if (score > winnerScore) {
            winnerScore = score
            winner = target
          }
        }
      }
      if (dy < 0) {
        val diff = target.itemView.top / 2 - curY
        if (diff > 0 && target.itemView.top < selected.itemView.top) {
          val score = abs(diff)
          if (score > winnerScore) {
            winnerScore = score
            winner = target
          }
        }
      }
      if (dy > 0) {
        val diff = target.itemView.bottom * 2 - bottom
        if (diff < 0 && target.itemView.bottom > selected.itemView.bottom) {
          val score = abs(diff)
          if (score > winnerScore) {
            winnerScore = score
            winner = target
          }
        }
      }
    }
    return winner
  }
}