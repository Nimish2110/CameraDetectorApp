package com.example.cameradetectorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cameradetectorapp.databinding.ActivityMagneticDetectionBinding
import kotlin.math.sqrt

class MagneticDetectionActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMagneticDetectionBinding
    private lateinit var sensorManager: SensorManager
    private var magneticSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMagneticDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (magneticSensor == null) {
            Toast.makeText(this, "No magnetic sensor found!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        magneticSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val totalMagField = sqrt((x * x + y * y + z * z).toDouble())
            val formatted = String.format("%.2f", totalMagField)
            binding.tvMagnetic.text = "Magnetic Field: $formatted µT"

            binding.tvStatus.text = if (totalMagField > 100) {
                "⚠️ Possible Magnetic Source Detected"
            } else {
                "Normal magnetic field"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed
    }
}
