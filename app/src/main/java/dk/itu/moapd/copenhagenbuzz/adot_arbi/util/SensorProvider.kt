package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Singleton object that detects shake gestures using the device's accelerometer sensor.
 *
 * You can initialize it with a [SensorManager] and a [ShakeListener] to start listening
 * for shake events, and call [stop] to unregister the sensor listener.
 */
object SensorProvider {

    private var sensorManager: SensorManager? = null
    private var listener: ShakeListener? = null

    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    /**
     * Initializes the shake detection system and registers a sensor listener.
     *
     * @param sensorManager The [SensorManager] used to access the accelerometer.
     * @param shakeListener A callback listener to be invoked when a shake is detected.
     */
    fun init(sensorManager: SensorManager, shakeListener: ShakeListener) {
        this.sensorManager = sensorManager
        this.listener = shakeListener

        sensorManager.registerListener(
            sensorEventListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    /**
     * Stops listening to sensor events and clears the shake listener reference.
     */
    fun stop() {
        sensorManager?.unregisterListener(sensorEventListener)
        listener = null
    }

    /**
     * Internal sensor event listener for detecting shake gestures.
     */
    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 10F) {
                listener?.onShake()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // No-op
        }
    }

    /**
     * Interface definition for a callback to be invoked when a shake is detected.
     */
    interface ShakeListener {
        /**
         * Called when a shake gesture is detected.
         */
        fun onShake()
    }
}
