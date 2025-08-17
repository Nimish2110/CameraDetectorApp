package com.example.cameradetectorapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cameradetectorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIrDetect.setOnClickListener {
            startActivity(Intent(this, IRDetectionActivity::class.java))
        }

        binding.btnMagneticDetect.setOnClickListener {
            startActivity(Intent(this, MagneticDetectionActivity::class.java))
        }
    }
}
