package com.example.myapplication.konjianlearn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.example.myapplication.databinding.AcitivityCanvasBinding

class LearnCanvasActivity: AppCompatActivity() {

    private lateinit var binding: AcitivityCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcitivityCanvasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnClickListener {
            ToastUtils.showShort("hi, outside", false)
        }

        initListeners()
    }

    private fun initListeners() {
        binding.text1.setOnClickListener {
            binding.measurePath.test = 0
            binding.measurePath.performClick()
        }
        binding.text2.setOnClickListener {
            binding.measurePath.test = 1
            binding.measurePath.performClick()
        }
        binding.text3.setOnClickListener {
            binding.measurePath.test = 2
            binding.measurePath.performClick()
        }
        binding.text4.setOnClickListener {
            binding.loadingView.startAnim()
        }
    }
}