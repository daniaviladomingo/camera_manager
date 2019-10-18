@file:Suppress("DEPRECATION")

package avila.domingo.camera.config

import android.hardware.Camera
import avila.domingo.domain.model.CameraSide

interface IConfigureCamera {
    fun configure(camera: Camera?, cameraSide: CameraSide)
}