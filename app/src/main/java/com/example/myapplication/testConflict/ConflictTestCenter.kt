package com.example.myapplication.testConflict

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.AcivityNewInterceptBinding
import com.example.myapplication.databinding.ActivityLayoutTestConflictBinding

class ConflictTestCenter: AppCompatActivity() {

    //private lateinit var binding: ActivityLayoutTestConflictBinding

    private lateinit var binding: AcivityNewInterceptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityLayoutTestConflictBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        binding = AcivityNewInterceptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.text.movementMethod = ScrollingMovementMethod.getInstance()
    }
}