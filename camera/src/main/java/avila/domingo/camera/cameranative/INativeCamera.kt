package avila.domingo.camera.cameranative

import android.hardware.Camera

interface INativeCamera {
    fun camera(): Camera
    fun cameraId(): Int
    fun switch(cameraId: Int)
    fun rotationDegrees(): Int
}