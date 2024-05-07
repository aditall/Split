package com.example.split1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.R
import com.example.split1.data.model.RoomItem
import com.example.split1.data.model.RoomSpace
import com.example.split1.ui.ImageUtil

class ItemAdapter(
    private val items: ArrayList<RoomItem>,
    private val itemClickListener: ItemAdapter.OnItemClickListener? = null
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var context: Context? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.title_image)
        val itemName: TextView = itemView.findViewById(R.id.tvHeading)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        context = parent.context
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSpace = items[position]
        context?.let { ImageUtil().laodImage(currentSpace.imageUrl.toUri(), it, holder.image) }
        holder.itemName.text = currentSpace.name
        holder.price.text = currentSpace.price

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    fun submitList(spaceList: List<RoomItem>) {
        this.items.clear()
        this.items.addAll(spaceList)
        notifyDataSetChanged()
    }
}