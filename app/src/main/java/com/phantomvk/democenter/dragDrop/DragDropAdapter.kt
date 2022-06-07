package com.phantomvk.democenter.dragDrop

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.R
import java.util.*

class DragDropAdapter(
  private val isForeground: Boolean,
  private val inflater: LayoutInflater,
  private val originList: List<DragDropModel>,
  private val sortedList: MutableList<DragDropModel>,
) : RecyclerView.Adapter<DragDropAdapter.ViewHolder>() {

  private val textBackgroundColor = ContextCompat.getColor(inflater.context, R.color.black50)

  fun onMove(
    viewHolder: RecyclerView.ViewHolder,
    target: RecyclerView.ViewHolder
  ) {
    val viewHolderPos = (viewHolder as MainViewHolder).bindingAdapterPosition
    val targetPos = (target as MainViewHolder).bindingAdapterPosition

    Collections.swap(sortedList, viewHolderPos, targetPos)

    notifyItemMoved(viewHolderPos, targetPos)
    viewHolder.itemView.post { notifyItemChanged(viewHolderPos) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return if (isForeground) {
      val v = inflater.inflate(R.layout.item_view_drag_drop_minor, parent, false)
      MainViewHolder(v, textBackgroundColor)
    } else {
      val v = inflater.inflate(R.layout.item_view_drag_drop_main, parent, false)
      MinorViewHolder(v, textBackgroundColor)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.onBind(originList[position], sortedList[position])
  }

  override fun getItemCount(): Int {
    return originList.size
  }

  abstract inner class ViewHolder(
    itemView: View,
    private val textBackgroundColor: Int
  ) : RecyclerView.ViewHolder(itemView) {

    protected val durationTextView: TextView = itemView.findViewById(R.id.textview_duration)

    open fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      setDurationText()
    }

    fun setDurationText() {
      val durationStr = originList[bindingAdapterPosition].duration
      durationTextView.text = String.format("%.1fs", durationStr)
      durationTextView.setBackgroundColor(textBackgroundColor)
    }
  }

  inner class MainViewHolder(
    itemView: View,
    textBackgroundColor: Int
  ) : ViewHolder(itemView, textBackgroundColor) {

    private val coverView: CardView = itemView.findViewById(R.id.cover_view)

    fun updateAnimation(isNotIdle: Boolean) {
      val preValue = if (isNotIdle) 1.0F else 0.0F
      val endValue = if (isNotIdle) 0.0F else 1.0F

      ValueAnimator.ofFloat(preValue, endValue)
        .setDuration(200L)
        .apply {
          addUpdateListener {
            val floatValue = (it.animatedValue as Float)

            durationTextView.alpha = floatValue
            durationTextView.background?.alpha = (floatValue * 255).toInt()

            val value = (1.0F - floatValue) * 0.14F + 1.0F
            coverView.scaleX = value
            coverView.scaleY = value
          }
        }
        .start()
    }

    override fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      super.onBind(originModel, targetModel)
      coverView.setCardBackgroundColor(targetModel.colorInt)
    }
  }

  inner class MinorViewHolder(
    itemView: View,
    backgroundColor: Int
  ) : ViewHolder(itemView, backgroundColor) {
    private val indexTextView: TextView = itemView.findViewById(R.id.textview_index)

    override fun onBind(originModel: DragDropModel, targetModel: DragDropModel) {
      super.onBind(originModel, targetModel)
      indexTextView.text = String.format("%s", bindingAdapterPosition + 1)
    }
  }
}