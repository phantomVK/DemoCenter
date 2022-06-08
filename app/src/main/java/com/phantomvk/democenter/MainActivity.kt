package com.phantomvk.democenter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phantomvk.democenter.aidl.AidlActivity
import com.phantomvk.democenter.bubbleShape.BubbleShapeActivity
import com.phantomvk.democenter.dragDrop.DragDropActivity

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val list = listOf(
      ButtonAbility("BubbleShape") {
        startActivity(Intent(this, BubbleShapeActivity::class.java))
      },

      ButtonAbility("AIDL in/out/inout") {
        startActivity(Intent(this, AidlActivity::class.java))
      },

      ButtonAbility("DragAndDrop") {
        startActivity(Intent(this, DragDropActivity::class.java))
      }
    )

    val adapter = ItemAdapter(layoutInflater, list)
    val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter
    adapter.notifyItemRangeInserted(0, list.size)
  }
}

private class ItemAdapter(
  private val inflater: LayoutInflater,
  private val list: List<DebugAbility>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = inflater.inflate(R.layout.item_view_button, parent, false)
    return ButtonViewHolder(view)
  }

  override fun getItemCount(): Int {
    return list.size
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (val item = list[position]) {
      is ButtonAbility -> {
        (holder as ButtonViewHolder).bind(item)
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return TYPE_BUTTON
  }

  private companion object {
    private const val TYPE_BUTTON = 0
  }

  private class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val button: Button = itemView.findViewById(R.id.button)

    fun bind(ability: ButtonAbility) {
      button.text = ability.text
      button.setOnClickListener(ability.listener)
    }
  }
}

private open class DebugAbility(val text: String)

private class ButtonAbility(
  text: String,
  val listener: (view: View) -> Unit
) : DebugAbility(text)