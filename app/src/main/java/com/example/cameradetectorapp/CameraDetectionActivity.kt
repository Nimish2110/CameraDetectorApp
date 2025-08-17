package com.example.cameradetectorapp

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraDetectionActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var camera: Camera? = null
    private var isCameraInUse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_detection)

        tvResult = findViewById(R.id.tvResult)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            detectCamera()
        }
    }

    private fun detectCamera() {
        try {
            camera = Camera.open()
            isCameraInUse = false
            tvResult.text = "No hidden camera detected."
            camera?.release()
        } catch (e: Exception) {
            isCameraInUse = true
            tvResult.text = "Hidden camera may be active!"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        camera?.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            detectCamera()
        } else {
            tvResult.text = "Camera permission denied."
        }
    }
}
