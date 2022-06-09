package com.example.teamproject_galaxy

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamproject_galaxy.databinding.RowFavBinding
import com.google.android.gms.maps.model.LatLng

class FavAdapter (val stnList: List<String>) : RecyclerView.Adapter<FavAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(stnList:String,position:Int)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowFavBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.favView.setOnClickListener{
                itemClickListener?.OnItemClick(stnList[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= RowFavBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.favView.text = stnList[position]+"역"
    }

    override fun getItemCount(): Int {
        return stnList.size
    }

}