package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.RecyclerItemViewBinding
import com.example.myapplication.utils.LaunchTimer
import com.example.myapplication.utils.WakeLockUtils

open class DAdapter(val context: Context): RecyclerView.Adapter<DAdapter.VH>(){

    private var mHasRecorded = false

    class VH(val binding: RecyclerItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RecyclerItemViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return 200
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!mHasRecorded && position == 0) {
            mHasRecorded = true
            holder.binding.root.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    LaunchTimer.endRecord()
                    holder.binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            })
        }
        holder.binding.root.text = "Hi." + position
        holder.binding.root.setOnClickListener {
            WakeLockUtils.getInstance().acquire(holder.binding.root.context)
            it.postDelayed({
                WakeLockUtils.getInstance().release()
            },200)
            //runnable?.run()
        }
    }

    var runnable: Runnable? = null
}