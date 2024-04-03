package com.example.chooseyourmarvel2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MarvelAdapter(private val marvelList: List<Triple<String, String, String>>) :
    RecyclerView.Adapter<MarvelAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val marvelImage: ImageView = view.findViewById(R.id.marvel_image)
        val characterName: TextView = view.findViewById(R.id.character_name)
        val characterDescription: TextView = view.findViewById(R.id.character_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.marvel_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = marvelList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (imageUrl, name, description) = marvelList[position]
        Glide.with(holder.itemView)
            .load(imageUrl)
            .centerCrop()
            .into(holder.marvelImage)
        holder.characterName.text = name
        holder.characterDescription.text = description
    }
}