package com.example.cameradetectorapp.utils

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class CameraAnalyzer(
    private val onResult: (Boolean) -> Unit // Callback function: true = IR detected
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        if (image.format != ImageFormat.YUV_420_888) {
            image.close()
            return
        }

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()

        var brightPixelCount = 0
        for (byte in data) {
            val value = byte.toInt() and 0xFF
            if (value > 250) {
                brightPixelCount++
            }
        }

        val brightnessRatio = brightPixelCount.toDouble() / data.size
        Log.d("CameraAnalyzer", "Bright pixel ratio: $brightnessRatio")

        val isIRDetected = brightnessRatio > 0.03
        onResult(isIRDetected)

        image.close()
    }

    // Convert ByteBuffer to ByteArray
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }
}
