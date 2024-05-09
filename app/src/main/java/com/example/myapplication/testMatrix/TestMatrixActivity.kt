package com.example.myapplication.testMatrix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityTestMatrxBinding

class TestMatrixActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestMatrxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestMatrxBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}