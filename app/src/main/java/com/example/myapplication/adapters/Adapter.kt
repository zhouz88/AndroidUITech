package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.RecyclerItemViewBinding

open class DAdapter(val context: Context): RecyclerView.Adapter<DAdapter.VH>(){

    class VH(val binding: RecyclerItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RecyclerItemViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return 200
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.root.text = "Hi." + position
    }
}