package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.dataClasses.OtherData
import org.w3c.dom.Text

class MyRecyclerAdapter(private val arrayList: ArrayList<OtherData>) :
    RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    var context: Context? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var image: ImageView = itemView.findViewById(R.id.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if (context == null) {
            context = parent.context
        }
        return MyViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = arrayList[position].title
        holder.description.text = arrayList[position].description
        Glide.with(context!!).load(arrayList[position].image).into(holder.image)

    }


}
