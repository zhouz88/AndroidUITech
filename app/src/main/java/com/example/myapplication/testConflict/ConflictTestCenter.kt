package com.example.myapplication.testConflict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLayoutTestConflictBinding

class ConflictTestCenter: AppCompatActivity() {

    private lateinit var binding: ActivityLayoutTestConflictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutTestConflictBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}