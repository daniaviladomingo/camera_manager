package avila.domingo.camera

import android.hardware.Camera

interface INativeCamera {
    fun camera(): Camera
}