package com.example.teamproject_galaxy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject_galaxy.databinding.RowBinding

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
        holder.binding.textView.text=items[position].subwayNm
        holder.binding.textView2.text=items[position].location
        holder.binding.textView2.text=items[position].direction.toString()
        holder.binding.textView2.text=items[position].LastSubway.toString()
    }
}