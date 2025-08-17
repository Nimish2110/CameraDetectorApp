package com.example.cameradetectorapp

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.cameradetectorapp.databinding.ActivityIrDetectionBinding
import com.example.cameradetectorapp.utils.CameraAnalyzer
import java.util.concurrent.Executors

class IRDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIrDetectionBinding
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    // Request camera permission
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIrDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start permission request
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // Image Analyzer
            val analyzer = ImageAnalysis.Builder().build().also { imageAnalysis ->
                imageAnalysis.setAnalyzer(cameraExecutor, CameraAnalyzer { isBright ->
                    runOnUiThread {
                        binding.tvStatus.text = if (isBright)
                            "⚠️ Possible IR Light Detected"
                        else
                            "No IR light detected"
                    }
                })
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer
                )
            } catch (e: Exception) {
                Log.e("IRDetection", "Failed to bind camera use cases", e)
            }

        }, ContextCompat.getMainExecutor(this))
    }
}
