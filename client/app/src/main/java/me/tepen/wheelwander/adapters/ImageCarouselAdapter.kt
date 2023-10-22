package me.tepen.wheelwander.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.tepen.wheelwander.R


class ImageCarouselAdapter(val context: Context?, val arrayList: ArrayList<String>) : RecyclerView.Adapter<ImageCarouselAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.image_carousel_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        Glide.with(context!!).load(arrayList[position]).into(holder.imageView)
        holder.itemView.setOnClickListener { view: View? ->
            onItemClickListener!!.onClick(
                holder.imageView,
                arrayList[position]
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.list_item_image)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView?, path: String?)
    }
}