package com.example.split1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.R
import com.example.split1.data.model.Spaces

class SpaceAdapter (private val spaceList : ArrayList<Spaces>) : RecyclerView.Adapter<SpaceAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.title_image)
        val spaceName: TextView = itemView.findViewById(R.id.tvHeading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sapce_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return spaceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSpace = spaceList[position]
        holder.image.setImageURI(currentSpace.image)
        holder.spaceName.text = currentSpace.name
    }
}