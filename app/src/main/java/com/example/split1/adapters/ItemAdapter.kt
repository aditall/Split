import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.split1.R
import com.example.split1.data.model.RoomItem
import com.example.split1.ui.ImageUtil
import com.google.firebase.auth.FirebaseAuth

class ItemAdapter(
    private val items: ArrayList<RoomItem>,
    private val itemClickListener: ItemClickListener? = null
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onDeleteButtonClick(position: Int)
        fun onEditButtonClick(position: Int)
    }

    var context: Context? = null
    private val currentUserUid: String? = FirebaseAuth.getInstance().currentUser?.uid

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.title_image)
        val itemName: TextView = itemView.findViewById(R.id.tvHeading)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val btnDeleteItem: Button = itemView.findViewById(R.id.btnDeleteItem)
        val btnEditItem: ImageButton = itemView.findViewById(R.id.btnEditItem)
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

        // Check if the current user is the publisher of the item
        if (currentSpace.publisher == currentUserUid) {
            holder.btnDeleteItem.visibility = View.VISIBLE
            holder.btnEditItem.visibility = View.VISIBLE
        } else {
            holder.btnDeleteItem.visibility = View.GONE
            holder.btnEditItem.visibility = View.GONE
        }

        holder.btnDeleteItem.setOnClickListener {
            itemClickListener?.onDeleteButtonClick(position)
        }

        holder.btnEditItem.setOnClickListener {
            itemClickListener?.onEditButtonClick(position)
        }
    }

    fun submitList(spaceList: List<RoomItem>) {
        this.items.clear()
        this.items.addAll(spaceList)
        notifyDataSetChanged()
    }
}
