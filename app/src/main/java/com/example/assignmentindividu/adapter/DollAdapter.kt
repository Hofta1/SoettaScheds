package com.example.assignmentindividu.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentindividu.R
import com.example.assignmentindividu.database.loadImage
import com.example.assignmentindividu.items.Doll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class DollAdapter(private val dollList: List<Doll>, private val itemClickListener: OnItemClickListener, private val coroutineScope: CoroutineScope): RecyclerView.Adapter<DollAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(item: Doll)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //tempat binding komponen doll_item
        val dollName = itemView.findViewById<TextView>(R.id.dollNameTV)
        val imageView = itemView.findViewById<ImageView>(R.id.dollIV)
        val cardView = itemView.findViewById<CardView>(R.id.dollCardView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doll_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dollList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Set data berdasarkan komponen
        val currentItem = dollList[position]
        holder.dollName.text = currentItem.name
//        holder.imageView.setImageResource(currentItem.path!!)
        coroutineScope.launch {
            loadImage(holder.imageView,currentItem.path!!)
        }
        holder.cardView.setOnClickListener{
            itemClickListener.onItemClick(currentItem)
        }
    }


}