package com.example.teamproject_galaxy

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject_galaxy.databinding.RowBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import org.jsoup.Jsoup

class SubwayAdapter(val items:ArrayList<Subway>): RecyclerView.Adapter<SubwayAdapter.MyViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }
    var itemClickListener:OnItemClickListener ?=null
    inner class MyViewHolder(val binding: com.example.teamproject_galaxy.databinding.RowBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.textView.setOnClickListener{
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= RowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.textView.text=items[position].subwayNm+items[position].location+items[position].direction.toString()+
                items[position].LastSubway.toString()+items[position].trainStatus
    }
}