package com.l.joshassignment.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.joshassignment.R
import com.l.joshassignment.helper.show
import com.l.joshassignment.responseModel.FlickerData
import kotlinx.android.synthetic.main.item_flicker.view.*
import kotlinx.android.synthetic.main.item_loading.view.*


class FilckrAdapter : RecyclerView.Adapter<FilckrAdapter.ViewHolder>() {
    private var itemList : MutableList<FlickerData> = mutableListOf()

    private val TYPE_LAST_ITEM = 999
    private val TYPE_ITEM = 0
    private val circleTransformation = RequestOptions.circleCropTransform()
        .error(R.drawable.ic_person_placeholder)
        .placeholder(R.drawable.ic_person_placeholder)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_ITEM -> {
                val item = itemList[position]
                val imageUel = "https://farm"+item.farm+".staticflickr.com/"+item.server+"/"+item.id+"_"+item.secret+".jpg"
                holder.itemView.important_tag_layout.text = item.title
                Glide.with(holder.itemView).asBitmap()
                    .load(imageUel)
                    .apply(circleTransformation)
                    .into(holder.itemView.message_primary_image)
            }
            TYPE_LAST_ITEM -> {
                holder.itemView.progress_bar.show()
            }
        }

    }

    fun setData(list: MutableList<FlickerData>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = when (viewType) {
            TYPE_ITEM -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_flicker, parent, false)
            }

            TYPE_LAST_ITEM -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            }

            else -> {
                //should not happen
                LayoutInflater.from(parent.context).inflate(R.layout.item_flicker, parent, false)
            }
        }
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return if (itemList.size == 0) 0 else itemList.size.plus(1)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> TYPE_LAST_ITEM
            else -> TYPE_ITEM
        }
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView)
}


