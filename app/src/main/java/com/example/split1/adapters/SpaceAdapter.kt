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
import com.example.split1.data.model.RoomSpace
import com.example.split1.ui.ImageUtil


class SpaceAdapter (private val spaceList : ArrayList<RoomSpace>, private val itemClickListener: OnItemClickListener? = null) : RecyclerView.Adapter<SpaceAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    var context: Context? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.title_image)
        val spaceName: TextView = itemView.findViewById(R.id.tvHeading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sapce_list, parent, false)
        context = parent.context
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return spaceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSpace = spaceList[position]
        context?.let { ImageUtil().laodImage(currentSpace.imageUrl.toUri(), it,holder.image) }
        holder.spaceName.text = currentSpace.name

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    fun submitList(spaceList: List<RoomSpace>){
        this.spaceList.clear()
        this.spaceList.addAll(spaceList)
        notifyDataSetChanged()
    }
}