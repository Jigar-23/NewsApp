package com.example.newsapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.WebView
import com.example.newsapp.dataClasses.OtherData


class MyRecyclerAdapter(
    private val arrayList: ArrayList<OtherData>,
    private val activity: Context
) :
    RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    private var context: Context? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var image: ImageView = itemView.findViewById(R.id.image)
        val cardView: CardView = itemView.findViewById(R.id.newsCard)

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
        holder.cardView.setOnClickListener {
            activity.startActivity(Intent(activity,WebView::class.java).apply {
                putExtra("url",arrayList[position].url)
            })
        }


    }


}
