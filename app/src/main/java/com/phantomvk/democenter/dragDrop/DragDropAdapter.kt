package com.phantomvk.democenter.dragDrop

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.R
import java.util.*

class DragDropAdapter(
  private val isForeground: Boolean,
  private val inflater: LayoutInflater,
  private val originList: List<DragDropModel>,
  private val sortedList: MutableList<DragDropModel>,
) : RecyclerView.Adapter<DragDropAdapter.ViewHolder>() {

  fun onMove(
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ) {
    val viewHolderPos = (viewHolder as MainViewHolder).bindingAdapterPosition
    val targetPos = (target as MainViewHolder).bindingAdapterPosition

    target.isDurationTextVisible(true)
    Collections.swap(sortedList, viewHolderPos, targetPos)

    notifyItemMoved(viewHolderPos, targetPos)
    viewHolder.itemView.post { notifyItemChanged(viewHolderPos) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return if (isForeground) {
      val v = inflater.inflate(R.layout.item_view_drag_drop_minor, parent, false)
      MainViewHolder(v)
    } else {
      val v = inflater.inflate(R.layout.item_view_drag_drop_main, parent, false)
      MinorViewHolder(v)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.onBind(originList[position], sortedList[position])
  }

  override fun getItemCount(): Int {
    return originList.size
  }

  abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected val durationTextView: TextView = itemView.findViewById(R.id.textview_duration)

    open fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      durationTextView.text = String.format("%.1fs", originModel.duration)
    }
  }

  inner class MainViewHolder(itemView: View) : ViewHolder(itemView) {
    private val coverView: CardView = itemView.findViewById(R.id.cover_view)

    fun isDurationTextVisible(visible: Boolean) {
      durationTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setDurationText() {
      val durationStr = originList[bindingAdapterPosition].duration
      durationTextView.text = String.format("%.1fs", durationStr)
    }

    fun setDurationTextViewAlpha(isNotIdle: Boolean) {
      val preScale = if (isNotIdle) 1.0F else 0.0F
      val endScale = if (isNotIdle) 0.0F else 1.0F

      ValueAnimator.ofFloat(preScale, endScale)
        .setDuration(200L)
        .apply {
          addUpdateListener {
            durationTextView.alpha = (it.animatedValue as Float)
          }
        }
        .start()
    }

    fun scaleCoverView(isUpScale: Boolean) {
      val preScale = if (isUpScale) 1.0F else 1.14F
      val endScale = if (isUpScale) 1.14F else 1.0F

      ValueAnimator.ofFloat(preScale, endScale)
        .setDuration(200L)
        .apply {
          addUpdateListener {
            val value = it.animatedValue as Float
            coverView.scaleX = value
            coverView.scaleY = value
          }
        }.start()
    }

    override fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      super.onBind(originModel, targetModel)
      coverView.setCardBackgroundColor(targetModel.colorInt)
    }
  }

  class MinorViewHolder(itemView: View) : ViewHolder(itemView) {
    private val indexTextView: TextView = itemView.findViewById(R.id.textview_index)

    override fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      super.onBind(originModel, targetModel)
      indexTextView.text = String.format("%s", bindingAdapterPosition + 1)
    }
  }
}