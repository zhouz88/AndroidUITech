package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivitySlideBinding

class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ViewDragLayoutBinding
//    private lateinit var binding: TextUpBinding
    private lateinit var binding: ActivitySlideBinding
    private val mDatas = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

    Log.d("zhouzheng", "2 oncreate")
        super.onCreate(savedInstanceState)
        // toggle button 代码
        //setContentView(R.layout.activity_main)

        //draglayout
//        binding = ViewDragLayoutBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.tv1.setOnClickListener {
//            Log.d("zhouzheng", "hi")
//            finish()
//            Toast.makeText(this, "hi",  Toast.LENGTH_SHORT)
//        }

//        binding = TextUpBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.image.setOnClickListener {
//            Log.d("zhouzheng", "hala")
//        }

       //rv
        binding = ActivitySlideBinding.inflate(layoutInflater)

        setContentView(binding.root)

        generateDatas()

        binding.rv.setLayoutManager(CustomLayoutManagerRecycled2())

        val adapter = RecyclerAdapter(this, mDatas)
        binding.rv.setAdapter(adapter)
    }

    private fun generateDatas() {
        for (i in 0..199) {
            mDatas.add("第 $i 个item")
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d("zhouzheng", "2 onstart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("zhouzheng", "2 onresume")
    }
}


class RecyclerAdapter(
    private val mContext: Context,
    private val mDatas: java.util.ArrayList<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var mCreatedHolder = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mCreatedHolder++
        //        Log.d("qijian", "onCreateViewHolder num:"+mCreatedHolder);
        val inflater = LayoutInflater.from(mContext)
        return NormalHolder(inflater.inflate(R.layout.item_layout, parent, false)).apply {
            context = mContext
        }
    }

    override fun getItemCount(): Int {
       return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        Log.d("qijian", "onBindViewHolder");
        val normalHolder = holder as NormalHolder
        normalHolder.mTV.text = mDatas[position]
    }

    class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTV: TextView

        var context: Context? = null

        init {
            mTV = itemView.findViewById<View>(R.id.text) as TextView
            mTV.setOnClickListener {
                Toast.makeText(context , mTV.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
